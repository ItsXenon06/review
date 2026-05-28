-- ============================================================
--  CAR RENTAL DATABASE  |  SQL Server Express
--  Features: Fleet management, Online booking/reservations,
--            Insurance & damage tracking, Dynamic pricing
--  Scope: Single branch
-- ============================================================

USE master;
GO

-- Safety guard: halt if DB already exists to prevent accidental data loss.
-- To reinstall from scratch, manually DROP DATABASE CarRentalDB first.
IF DB_ID('CarRentalDB') IS NOT NULL
BEGIN
    RAISERROR('Database CarRentalDB already exists. Drop it manually before re-running this script.', 16, 1);
    SET NOEXEC ON;
END
GO

CREATE DATABASE CarRentalDB;
GO

USE CarRentalDB;
SET NOEXEC OFF; -- re-enable execution after the safety guard above
GO

-- ============================================================
--  SECTION 1 – LOOKUP / REFERENCE TABLES
-- ============================================================

CREATE TABLE VehicleCategory (
                                 CategoryID      INT           IDENTITY(1,1) PRIMARY KEY,
                                 CategoryName    NVARCHAR(50)  NOT NULL,          -- Economy, Compact, SUV, Luxury …
                                 Description     NVARCHAR(255) NULL,
                                 BaseDailyRate   DECIMAL(10,2) NOT NULL,          -- anchor rate for dynamic pricing
                                 CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE PricingRule (
                             RuleID          INT           IDENTITY(1,1) PRIMARY KEY,
                             RuleName        NVARCHAR(100) NOT NULL,
                             RuleType        NVARCHAR(30)  NOT NULL            -- 'PEAK_SEASON','WEEKEND','DEMAND','PROMO'
        CHECK (RuleType IN ('PEAK_SEASON','WEEKEND','DEMAND','PROMO')),
                             MultiplierPct   DECIMAL(5,2)  NOT NULL CHECK (MultiplierPct BETWEEN 0.10 AND 5.00), -- e.g. 1.30 = +30 %, 0.85 = -15 %
                             StartDate       DATE          NULL,               -- NULL = always active for rule type
                             EndDate         DATE          NULL,
                             DaysOfWeek      NVARCHAR(20)  NULL,               -- '1,7' = Mon & Sun (ISO: 1=Mon)
                             MinOccupancyPct DECIMAL(5,2)  NULL,               -- demand trigger (% fleet rented out)
                             IsActive        BIT           NOT NULL DEFAULT 1,
                             CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE InsurancePlan (
                               PlanID          INT           IDENTITY(1,1) PRIMARY KEY,
                               PlanName        NVARCHAR(100) NOT NULL,           -- Basic, Standard, Full Coverage
                               DailyPremium    DECIMAL(10,2) NOT NULL,
                               DeductibleAmt   DECIMAL(10,2) NOT NULL,
                               CoverageDetails NVARCHAR(MAX) NULL,
                               IsActive        BIT           NOT NULL DEFAULT 1
);

-- ============================================================
--  SECTION 2 – FLEET
-- ============================================================

CREATE TABLE Vehicle (
                         VehicleID       INT           IDENTITY(1,1) PRIMARY KEY,
                         CategoryID      INT           NOT NULL REFERENCES VehicleCategory(CategoryID),
                         LicensePlate    NVARCHAR(20)  NOT NULL UNIQUE,
                         Make            NVARCHAR(50)  NOT NULL,
                         Model           NVARCHAR(50)  NOT NULL,
                         Year            SMALLINT      NOT NULL,
                         Color           NVARCHAR(30)  NULL,
                         VIN             NVARCHAR(17)  NOT NULL UNIQUE,
                         FuelType        NVARCHAR(20)  NOT NULL DEFAULT 'Gasoline'
        CHECK (FuelType IN ('Gasoline','Diesel','Hybrid','Electric')),
                         TransmissionType NVARCHAR(10) NOT NULL DEFAULT 'Automatic'
        CHECK (TransmissionType IN ('Automatic','Manual')),
                         SeatingCapacity TINYINT       NOT NULL DEFAULT 5,
                         CurrentMileage  INT           NOT NULL DEFAULT 0,
                         Status          NVARCHAR(20)  NOT NULL DEFAULT 'Available'
        CHECK (Status IN ('Available','Rented','Maintenance','Retired')),
                         LastServiceDate DATE          NULL,
                         Notes           NVARCHAR(500) NULL,
                         CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                         UpdatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE MaintenanceRecord (
                                   RecordID        INT           IDENTITY(1,1) PRIMARY KEY,
                                   VehicleID       INT           NOT NULL REFERENCES Vehicle(VehicleID),
                                   MaintenanceType NVARCHAR(50)  NOT NULL   -- 'Routine','Repair','Inspection','Recall'
        CHECK (MaintenanceType IN ('Routine','Repair','Inspection','Recall','Tire','Oil Change')),
                                   Description     NVARCHAR(500) NULL,
                                   MileageAtService INT          NOT NULL,
                                   ServiceDate     DATE          NOT NULL,
                                   NextServiceDate DATE          NULL,
                                   Cost            DECIMAL(10,2) NULL,
                                   ServiceProvider NVARCHAR(100) NULL,
                                   CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

-- ============================================================
--  SECTION 3 – CUSTOMERS
-- ============================================================

CREATE TABLE Customer (
                          CustomerID      INT           IDENTITY(1,1) PRIMARY KEY,
                          FirstName       NVARCHAR(100) NOT NULL,
                          LastName        NVARCHAR(100) NOT NULL,
                          Email           NVARCHAR(255) NOT NULL UNIQUE,
                          PhoneNumber     NVARCHAR(20)  NULL,
                          DateOfBirth     DATE          NULL,
                          DriverLicenseNo NVARCHAR(50)  NOT NULL UNIQUE,
                          LicenseExpiry   DATE          NOT NULL,
                          LicenseCountry  NVARCHAR(50)  NOT NULL DEFAULT 'VN',
                          BlacklistFlag   BIT           NOT NULL DEFAULT 0,
                          BlacklistReason NVARCHAR(255) NULL,
                          CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                          UpdatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

-- ============================================================
--  SECTION 4 – RESERVATIONS & RENTALS
-- ============================================================

CREATE TABLE Reservation (
                             ReservationID   INT           IDENTITY(1,1) PRIMARY KEY,
                             CustomerID      INT           NOT NULL REFERENCES Customer(CustomerID),
                             VehicleID       INT           NOT NULL REFERENCES Vehicle(VehicleID),
                             InsurancePlanID INT           NULL     REFERENCES InsurancePlan(PlanID),
                             PickupDate      DATE          NOT NULL,
                             ReturnDate      DATE          NOT NULL,
                             TotalDays       AS DATEDIFF(DAY, PickupDate, ReturnDate) PERSISTED,
                             QuotedDailyRate DECIMAL(10,2) NOT NULL,   -- rate locked at booking time
                             EstimatedTotal  DECIMAL(10,2) NOT NULL,
                             Status          NVARCHAR(20)  NOT NULL DEFAULT 'Pending'
        CHECK (Status IN ('Pending','Confirmed','Active','Completed','Cancelled','NoShow')),
                             SpecialRequests NVARCHAR(500) NULL,
                             CancelledAt     DATETIME2     NULL,
                             CancelReason    NVARCHAR(255) NULL,
                             CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                             UpdatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                             CONSTRAINT CHK_ReturnAfterPickup CHECK (ReturnDate > PickupDate)
);

-- Applied pricing rules per reservation (many-to-many)
CREATE TABLE ReservationPricingRule (
                                        ReservationID   INT           NOT NULL REFERENCES Reservation(ReservationID),
                                        RuleID          INT           NOT NULL REFERENCES PricingRule(RuleID),
                                        AppliedMultiplier DECIMAL(5,2) NOT NULL,
                                        PRIMARY KEY (ReservationID, RuleID)
);

CREATE TABLE Rental (
                        RentalID            INT           IDENTITY(1,1) PRIMARY KEY,
                        ReservationID       INT           NOT NULL UNIQUE REFERENCES Reservation(ReservationID),
                        ActualPickupDate    DATETIME2     NOT NULL,
                        ActualReturnDate    DATETIME2     NULL,
                        MileageOut          INT           NOT NULL,
                        MileageIn           INT           NULL,
                        CONSTRAINT CHK_MileageIn CHECK (MileageIn IS NULL OR MileageIn >= MileageOut),
                        FuelLevelOut        NVARCHAR(10)  NOT NULL DEFAULT 'Full'
        CHECK (FuelLevelOut  IN ('Full','3/4','1/2','1/4','Empty')),
                        FuelLevelIn         NVARCHAR(10)  NULL
        CHECK (FuelLevelIn   IN ('Full','3/4','1/2','1/4','Empty')),
                        BaseRentalCharge    DECIMAL(10,2) NULL,     -- filled on return
                        InsuranceCharge     DECIMAL(10,2) NULL,
                        ExtraCharges        DECIMAL(10,2) NOT NULL DEFAULT 0,  -- late return, fuel diff …
                        ExtraChargesNote    NVARCHAR(255) NULL,
                        TotalCharge         DECIMAL(10,2) NULL,     -- filled on return
                        StaffPickupNotes    NVARCHAR(500) NULL,
                        StaffReturnNotes    NVARCHAR(500) NULL,
                        CreatedAt           DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                        UpdatedAt           DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

-- ============================================================
--  SECTION 5 – INSURANCE & DAMAGE TRACKING
-- ============================================================

CREATE TABLE DamageReport (
                              DamageID        INT           IDENTITY(1,1) PRIMARY KEY,
                              RentalID        INT           NOT NULL REFERENCES Rental(RentalID),
                              ReportedAt      DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                              DamageType      NVARCHAR(50)  NOT NULL
        CHECK (DamageType IN ('Scratch','Dent','Glass','Mechanical','Flood','Total Loss','Other')),
                              Location        NVARCHAR(100) NULL,   -- e.g. 'Front bumper, left side'
                              Description     NVARCHAR(1000) NULL,
                              EstimatedRepairCost DECIMAL(10,2) NULL,
                              ActualRepairCost    DECIMAL(10,2) NULL,
                              CustomerLiable  BIT           NOT NULL DEFAULT 1,
                              InsuranceClaimed BIT          NOT NULL DEFAULT 0
);

CREATE TABLE InsuranceClaim (
                                ClaimID         INT           IDENTITY(1,1) PRIMARY KEY,
                                DamageID        INT           NOT NULL UNIQUE REFERENCES DamageReport(DamageID), -- UNIQUE: one claim per damage
                                InsurancePlanID INT           NOT NULL REFERENCES InsurancePlan(PlanID),
                                ClaimDate       DATE          NOT NULL DEFAULT CAST(SYSDATETIME() AS DATE),
                                ClaimStatus     NVARCHAR(20)  NOT NULL DEFAULT 'Open'
        CHECK (ClaimStatus IN ('Open','UnderReview','Approved','Rejected','Settled')),
                                ClaimAmount     DECIMAL(10,2) NOT NULL,
                                DeductibleCharged DECIMAL(10,2) NOT NULL DEFAULT 0,
                                SettledAmount   DECIMAL(10,2) NULL,
                                SettledAt       DATETIME2     NULL,
                                Notes           NVARCHAR(500) NULL,
                                CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME()
);

-- ============================================================
--  SECTION 6 – PAYMENTS
-- ============================================================

CREATE TABLE Payment (
                         PaymentID       INT           IDENTITY(1,1) PRIMARY KEY,
    -- A payment belongs to either a Reservation (e.g. deposit) or a Rental (charge/refund).
    -- Exactly one of these must be set — enforced by the CHECK below.
                         ReservationID   INT           NULL REFERENCES Reservation(ReservationID),
                         RentalID        INT           NULL REFERENCES Rental(RentalID),
                         PaymentDate     DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                         Amount          DECIMAL(10,2) NOT NULL CHECK (Amount > 0),
                         PaymentMethod   NVARCHAR(30)  NOT NULL
        CHECK (PaymentMethod IN ('Cash','CreditCard','DebitCard','BankTransfer','QR','Other')),
                         ReferenceNo     NVARCHAR(100) NULL,        -- card auth / bank ref
                         PaymentType     NVARCHAR(20)  NOT NULL DEFAULT 'Charge'
        CHECK (PaymentType IN ('Deposit','Charge','Refund')),
                         Notes           NVARCHAR(255) NULL,
                         CreatedAt       DATETIME2     NOT NULL DEFAULT SYSDATETIME(),
                         CONSTRAINT CHK_Payment_OneParent CHECK (
                             (ReservationID IS NOT NULL AND RentalID IS NULL) OR
                             (ReservationID IS NULL     AND RentalID IS NOT NULL)
                             )
);

-- ============================================================
--  SECTION 7 – INDEXES  (performance essentials)
-- ============================================================

CREATE INDEX IX_Vehicle_Status          ON Vehicle(Status);
CREATE INDEX IX_Vehicle_Category        ON Vehicle(CategoryID);
CREATE INDEX IX_Reservation_Customer    ON Reservation(CustomerID);
CREATE INDEX IX_Reservation_Vehicle     ON Reservation(VehicleID);
CREATE INDEX IX_Reservation_Dates       ON Reservation(PickupDate, ReturnDate);
CREATE INDEX IX_Reservation_Status      ON Reservation(Status);
CREATE INDEX IX_Rental_Reservation      ON Rental(ReservationID);
CREATE INDEX IX_Maintenance_Vehicle     ON MaintenanceRecord(VehicleID);
CREATE INDEX IX_DamageReport_Rental     ON DamageReport(RentalID);
CREATE INDEX IX_InsuranceClaim_Damage   ON InsuranceClaim(DamageID);
CREATE INDEX IX_Payment_Reservation    ON Payment(ReservationID);
CREATE INDEX IX_Payment_Rental          ON Payment(RentalID);
CREATE INDEX IX_Customer_Email          ON Customer(Email);
CREATE INDEX IX_Customer_License        ON Customer(DriverLicenseNo);

-- ============================================================
--  SECTION 8 – SEED DATA (enough to smoke-test)
-- ============================================================

INSERT INTO VehicleCategory (CategoryName, Description, BaseDailyRate) VALUES
                                                                           ('Economy',   'Small, fuel-efficient city cars',     350000),
                                                                           ('Compact',   'Mid-size sedans and hatchbacks',      500000),
                                                                           ('SUV',       '7-seat sport utility vehicles',       900000),
                                                                           ('Luxury',    'Premium executive cars',             1800000),
                                                                           ('Van',       '9–16 seat passenger vans',           1200000);

INSERT INTO InsurancePlan (PlanName, DailyPremium, DeductibleAmt, CoverageDetails) VALUES
                                                                                       ('Basic CDW',    50000,  5000000, 'Collision Damage Waiver only. Excludes theft, glass, tyres.'),
                                                                                       ('Standard',    120000,  2000000, 'CDW + theft protection + glass & tyre cover.'),
                                                                                       ('Full Cover',  200000,        0, 'Zero-deductible full coverage including personal accident.');

INSERT INTO PricingRule (RuleName, RuleType, MultiplierPct, StartDate, EndDate, DaysOfWeek, MinOccupancyPct) VALUES
                                                                                                                 ('Tet Holiday Peak',   'PEAK_SEASON', 1.50, '2026-01-26', '2026-02-05', NULL,  NULL),
                                                                                                                 ('Summer Peak',        'PEAK_SEASON', 1.30, '2026-06-01', '2026-08-31', NULL,  NULL),
                                                                                                                 ('Weekend Surcharge',  'WEEKEND',     1.15, NULL,          NULL,         '6,7', NULL),
                                                                                                                 ('High Demand +20%',   'DEMAND',      1.20, NULL,          NULL,         NULL,  80.00),
                                                                                                                 ('High Demand +10%',   'DEMAND',      1.10, NULL,          NULL,         NULL,  60.00),
                                                                                                                 ('Low Season Promo',   'PROMO',       0.85, '2026-09-01', '2026-11-30', NULL,  NULL);

INSERT INTO Vehicle (CategoryID, LicensePlate, Make, Model, Year, Color, VIN, FuelType, TransmissionType, SeatingCapacity, CurrentMileage, Status) VALUES
                                                                                                                                                       (1, '51A-12345', 'Toyota',   'Vios',       2022, 'White',  'JTDBR32E420012345', 'Gasoline', 'Automatic', 5,  45000, 'Available'),
                                                                                                                                                       (1, '51A-23456', 'Hyundai',  'Accent',     2023, 'Silver', 'KMHCN41ABPU023456', 'Gasoline', 'Automatic', 5,  28000, 'Available'),
                                                                                                                                                       (2, '51B-34567', 'Mazda',    'Mazda3',     2023, 'Red',    'JM1BM1W75D0034567', 'Gasoline', 'Automatic', 5,  32000, 'Available'),
                                                                                                                                                       (3, '51C-45678', 'Toyota',   'Fortuner',   2022, 'Black',  'MR0GX8FS8N0045678', 'Diesel',   'Automatic', 7,  60000, 'Available'),
                                                                                                                                                       (3, '51C-56789', 'Ford',     'Everest',    2023, 'Gray',   'MNBPXXMJ6NM056789', 'Diesel',   'Automatic', 7,  41000, 'Maintenance'),
                                                                                                                                                       (4, '51D-67890', 'Mercedes', 'E200',       2023, 'Black',  'WDD2130561A067890', 'Gasoline', 'Automatic', 5,  18000, 'Available'),
                                                                                                                                                       (5, '51E-78901', 'Ford',     'Transit 16', 2022, 'White',  'WF0EXXTTFEK078901', 'Diesel',   'Manual',   16,  85000, 'Available');

INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, DateOfBirth, DriverLicenseNo, LicenseExpiry, LicenseCountry) VALUES
                                                                                                                                ('Nguyen', 'Van An',   'an.nguyen@email.com',  '0901234567', '1990-05-15', 'VN-B2-0012345', '2028-05-15', 'VN'),
                                                                                                                                ('Tran',   'Thi Bich', 'bich.tran@email.com',  '0912345678', '1985-11-20', 'VN-B2-0023456', '2027-11-20', 'VN'),
                                                                                                                                ('Le',     'Minh Duc', 'duc.le@email.com',      '0923456789', '1995-03-08', 'VN-B2-0034567', '2029-03-08', 'VN');

GO
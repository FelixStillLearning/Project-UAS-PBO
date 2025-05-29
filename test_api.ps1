# Coffee Shop API Comprehensive Test Script
param(
    [string]$BaseUrl = "http://localhost:8080/api",
    [string]$Username = "admin", 
    [string]$Password = "admin123"
)

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "   Coffee Shop API Testing Script" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Create credentials
$auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${Username}:${Password}"))
$headers = @{
    "Authorization" = "Basic $auth"
    "Content-Type" = "application/json"
}

# Test results array
$testResults = @()

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method = "GET",
        [string]$Endpoint,
        [hashtable]$Headers = $headers,
        [string]$Body = $null
    )
    
    try {
        Write-Host "Testing $Name..." -ForegroundColor Yellow
        
        $params = @{
            Uri = "$BaseUrl$Endpoint"
            Method = $Method
            Headers = $Headers
            TimeoutSec = 10
        }
        
        if ($Body) {
            $params.Body = $Body
        }
        
        $response = Invoke-RestMethod @params -ErrorAction Stop
        Write-Host "✓ $Name - SUCCESS (200 OK)" -ForegroundColor Green
        
        # Show sample data if it's an array or object
        if ($response -is [array] -and $response.Count -gt 0) {
            Write-Host "  → Found $($response.Count) items" -ForegroundColor Gray
        } elseif ($response -is [object] -and $response.PSObject.Properties.Count -gt 0) {
            Write-Host "  → Response received" -ForegroundColor Gray
        }
        
        $script:testResults += [PSCustomObject]@{
            Test = $Name
            Status = "PASS"
            StatusCode = "200"
            Method = $Method
            Endpoint = $Endpoint
        }
        
        return $true
    }
    catch {
        $statusCode = if ($_.Exception.Response) { $_.Exception.Response.StatusCode } else { "Connection Failed" }
        Write-Host "✗ $Name - FAILED ($statusCode)" -ForegroundColor Red
        Write-Host "  → Error: $($_.Exception.Message)" -ForegroundColor Red
        
        $script:testResults += [PSCustomObject]@{
            Test = $Name
            Status = "FAIL"
            StatusCode = $statusCode
            Method = $Method
            Endpoint = $Endpoint
            Error = $_.Exception.Message
        }
        
        return $false
    }
}

Write-Host "Starting API endpoint tests..." -ForegroundColor White
Write-Host ""

# Authentication & Health Check
Test-Endpoint "Authentication Check" "GET" "/auth/me"

# Categories
Test-Endpoint "Get All Categories" "GET" "/categories"
Test-Endpoint "Get Category by ID" "GET" "/categories/1"

# Products  
Test-Endpoint "Get All Products" "GET" "/products"
Test-Endpoint "Get Product by ID" "GET" "/products/1"
Test-Endpoint "Get Products by Category" "GET" "/products/category/1"
Test-Endpoint "Search Products" "GET" "/products/search?name=coffee"
Test-Endpoint "Get Products with Pagination" "GET" "/products/page?size=5&page=0"

# Payment Methods
Test-Endpoint "Get All Payment Methods" "GET" "/payment-methods"
Test-Endpoint "Get Payment Method by ID" "GET" "/payment-methods/1"

# Customizations
Test-Endpoint "Get All Customizations" "GET" "/customizations"
Test-Endpoint "Get Customization by ID" "GET" "/customizations/1"
Test-Endpoint "Get Free Customizations" "GET" "/customizations/free"
Test-Endpoint "Get Paid Customizations" "GET" "/customizations/paid"
Test-Endpoint "Search Customizations" "GET" "/customizations/search?name=sugar"

# Stock Management
Test-Endpoint "Get Stock Info" "GET" "/stock/info/1"
Test-Endpoint "Get All Stock Info" "GET" "/stock/all"
Test-Endpoint "Get Low Stock Products" "GET" "/stock/low-stock?threshold=10"

# Reports
Test-Endpoint "Get Daily Kasir Report" "GET" "/reports/kasir/daily?date=2024-01-01"
Test-Endpoint "Get Transaction Report" "GET" "/reports/transactions?startDate=2024-01-01&endDate=2024-12-31"
Test-Endpoint "Get Product Sales Report" "GET" "/reports/product-sales?startDate=2024-01-01&endDate=2024-12-31"

# Orders (Read operations)
Test-Endpoint "Get All Orders (Admin)" "GET" "/orders/admin/all?size=5&page=0"

# Test POST endpoints with sample data
Write-Host ""
Write-Host "Testing POST endpoints..." -ForegroundColor Yellow

# Create Category
$categoryBody = @{
    name = "Test Category $(Get-Date -Format 'yyyyMMddHHmmss')"
} | ConvertTo-Json

Test-Endpoint "Create Category" "POST" "/categories" $headers $categoryBody

# Create Product
$productBody = @{
    name = "Test Product $(Get-Date -Format 'yyyyMMddHHmmss')"
    description = "A test product created by API test"
    price = 25000
    imageUrl = "https://example.com/test-product.jpg"
    categoryId = 1
} | ConvertTo-Json

Test-Endpoint "Create Product" "POST" "/products" $headers $productBody

# Create Payment Method
$paymentMethodBody = @{
    name = "Test Payment $(Get-Date -Format 'yyyyMMddHHmmss')"
    description = "A test payment method"
} | ConvertTo-Json

Test-Endpoint "Create Payment Method" "POST" "/payment-methods" $headers $paymentMethodBody

# Create Customization
$customizationBody = @{
    name = "Test Customization $(Get-Date -Format 'yyyyMMddHHmmss')"
    type = "SUGAR"
    priceAdjustment = 0
    description = "A test customization"
} | ConvertTo-Json

Test-Endpoint "Create Customization" "POST" "/customizations" $headers $customizationBody

# Test Kasir Order Creation
$kasirOrderBody = @{
    customerName = "Walk-in Customer $(Get-Date -Format 'yyyyMMddHHmmss')"
    orderItems = @(
        @{
            productId = 1
            quantity = 2
            customizations = @()
        }
    )
    paymentMethodId = 1
    notes = "Test order from API testing script"
} | ConvertTo-Json -Depth 3

Test-Endpoint "Create Kasir Order" "POST" "/orders/kasir" $headers $kasirOrderBody

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "   Test Results Summary" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

$passedTests = ($testResults | Where-Object { $_.Status -eq "PASS" }).Count
$failedTests = ($testResults | Where-Object { $_.Status -eq "FAIL" }).Count
$totalTests = $testResults.Count

Write-Host "Total Tests: $totalTests" -ForegroundColor White
Write-Host "Passed: $passedTests" -ForegroundColor Green
Write-Host "Failed: $failedTests" -ForegroundColor Red
Write-Host "Success Rate: $([math]::Round(($passedTests / $totalTests) * 100, 2))%" -ForegroundColor Cyan

if ($failedTests -gt 0) {
    Write-Host ""
    Write-Host "Failed Tests Details:" -ForegroundColor Red
    $testResults | Where-Object { $_.Status -eq "FAIL" } | ForEach-Object {
        Write-Host "  ✗ $($_.Test) - $($_.Method) $($_.Endpoint)" -ForegroundColor Red
        if ($_.Error) {
            Write-Host "    Error: $($_.Error)" -ForegroundColor Gray
        }
    }
}

Write-Host ""
Write-Host "API Testing Complete!" -ForegroundColor Green

# Export results to CSV
$testResults | Export-Csv -Path "api_test_results_$(Get-Date -Format 'yyyyMMdd_HHmmss').csv" -NoTypeInformation
Write-Host "Results exported to: api_test_results_$(Get-Date -Format 'yyyyMMdd_HHmmss').csv" -ForegroundColor Gray

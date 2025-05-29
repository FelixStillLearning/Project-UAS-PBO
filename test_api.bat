@echo off
echo ====================================
echo   Coffee Shop API Testing Script
echo ====================================
echo.

REM Set base URL
set BASE_URL=http://localhost:8080/api

echo [INFO] Testing Coffee Shop API endpoints...
echo.

echo [1/8] Testing Health Check...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/auth/me -u admin:admin123 2>nul || echo "Server not responding"
echo.

echo [2/8] Testing Categories...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/categories -u admin:admin123 2>nul || echo "Categories endpoint failed"
echo.

echo [3/8] Testing Products...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/products -u admin:admin123 2>nul || echo "Products endpoint failed"
echo.

echo [4/8] Testing Payment Methods...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/payment-methods -u admin:admin123 2>nul || echo "Payment Methods endpoint failed"
echo.

echo [5/8] Testing Customizations...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/customizations -u admin:admin123 2>nul || echo "Customizations endpoint failed"
echo.

echo [6/8] Testing Stock...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/stock/info/1 -u admin:admin123 2>nul || echo "Stock endpoint failed"
echo.

echo [7/8] Testing Reports...
curl -s -w "Status: %%{http_code}\n" %BASE_URL%/reports/kasir/daily?date=2024-01-01 -u admin:admin123 2>nul || echo "Reports endpoint failed"
echo.

echo [8/8] Testing Order Creation (POST)...
curl -X POST -H "Content-Type: application/json" -s -w "Status: %%{http_code}\n" ^
-d "{\"customerName\":\"Test Customer\",\"orderItems\":[{\"productId\":1,\"quantity\":1,\"customizations\":[]}],\"paymentMethodId\":1,\"notes\":\"Test order\"}" ^
%BASE_URL%/orders/kasir -u admin:admin123 2>nul || echo "Order creation failed"
echo.

echo ====================================
echo   API Testing Complete
echo ====================================
pause

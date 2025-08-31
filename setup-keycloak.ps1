# Wait for Keycloak to be ready
Start-Sleep -Seconds 10

# Create realm
$realmJson = @{
    realm = "globalbooks"
    enabled = $true
    displayName = "GlobalBooks Realm"
} | ConvertTo-Json

Invoke-RestMethod -Uri 'http://localhost:8180/admin/realms' -Method Post -Body $realmJson -ContentType 'application/json' -Headers @{
    Authorization = "Bearer $(Get-KeycloakAdminToken)"
}

# Create client
$clientJson = @{
    clientId = "globalbooks-client"
    enabled = $true
    publicClient = $true
    directAccessGrantsEnabled = $true
    standardFlowEnabled = $true
    redirectUris = @("*")
} | ConvertTo-Json

Invoke-RestMethod -Uri 'http://localhost:8180/admin/realms/globalbooks/clients' -Method Post -Body $clientJson -ContentType 'application/json' -Headers @{
    Authorization = "Bearer $(Get-KeycloakAdminToken)"
}

# Create user
$userJson = @{
    username = "testuser"
    enabled = $true
    emailVerified = $true
    credentials = @(
        @{
            type = "password"
            value = "testpass123"
            temporary = $false
        }
    )
} | ConvertTo-Json

Invoke-RestMethod -Uri 'http://localhost:8180/admin/realms/globalbooks/users' -Method Post -Body $userJson -ContentType 'application/json' -Headers @{
    Authorization = "Bearer $(Get-KeycloakAdminToken)"
}

Write-Host "Keycloak setup completed. You can now get a token using:"
Write-Host "Username: testuser"
Write-Host "Password: testpass123"

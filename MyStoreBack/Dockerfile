﻿FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
COPY ["./Uploads/", "./Uploads/"]
EXPOSE 80 
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["MyStoreBack.csproj", "./"]
RUN dotnet restore "MyStoreBack.csproj"
COPY . .
WORKDIR "/src/"
RUN dotnet build "MyStoreBack.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "MyStoreBack.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "MyStoreBack.dll"]

using System.Net;
using System.Net.Mime;
using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;
using Microsoft.IdentityModel.Tokens;
using MyStoreBack.Business_logic.Authentication;
using MyStoreBack.Business_logic.Category;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Data.Seeder;
using MyStoreBack.Mapper;
using MyStoreBack.Repository.Security;
using MyStoreBack.Repository.User;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddCors();

builder.Services.AddAutoMapper(typeof(MapperProfile));

// Database context
builder.Services.AddDbContext<StoreDbContext>(opts =>
{
    var conStr = builder.Environment.IsDevelopment()
        ? builder.Configuration.GetConnectionString("WebStoreConnection")
        : Environment.GetEnvironmentVariable("WebStoreConnection")
            ?? throw new Exception("Connection string not found.");
    opts.UseNpgsql(conStr);
});

// Identity configurations
builder.Services.AddIdentity<UserEntity, RoleEntity>(opts =>
{
    opts.Stores.MaxLengthForKeys = 512;
    // Password requirements
    opts.Password.RequiredLength = 8;
    opts.Password.RequireDigit = true;
    opts.Password.RequireUppercase = true;
    opts.Password.RequireLowercase = true;
    opts.Password.RequireNonAlphanumeric = true;
}).AddEntityFrameworkStores<StoreDbContext>().AddDefaultTokenProviders();

// Auth configurations
var keyBytes = Encoding.UTF8.GetBytes(builder.Configuration["JwtSecretKey"]);
SymmetricSecurityKey signingKey = new(keyBytes);
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(cfg =>
{
    cfg.RequireHttpsMetadata = false;
    cfg.SaveToken = true;
    cfg.TokenValidationParameters = new TokenValidationParameters
    {
        IssuerSigningKey = signingKey,
        ValidateAudience = false,
        ValidateIssuer = true,
        ValidIssuer = builder.Configuration["JwtIssuer"],
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ClockSkew = TimeSpan.Zero
    };
});

// JWT token service injection
builder.Services.AddScoped<IJwtTokenService, JwtTokenServiceImpl>();
builder.Services.AddScoped<ITokenRepository, TokenRepository>();

// Other services injection
builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped<ICategoryService, CategoryService>();

// Configure the port in the Heroku way
if (!builder.Environment.IsDevelopment())
    builder.WebHost.ConfigureKestrel(serverOptions =>
    {
        serverOptions.Listen(
            IPAddress.Any, 
            Convert.ToInt32(Environment.GetEnvironmentVariable("PORT"))
        );
    });

var app = builder.Build();

app.UseCors(policyBuilder => policyBuilder
    .AllowAnyHeader()
    .AllowAnyOrigin()
    .AllowAnyMethod()
);

await app.SeedDatabase();

var path = Path.Combine(Directory.GetCurrentDirectory(), "Uploads");
if (!Directory.Exists(path)) Directory.CreateDirectory(path);

app.UseStaticFiles(new StaticFileOptions
{
    FileProvider = new PhysicalFileProvider(path),
    RequestPath = "/api/uploads",
});

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
} 
else
{
    app.UseStatusCodePages(async context =>
    {
        context.HttpContext.Response.ContentType = MediaTypeNames.Text.Plain;
        await context.HttpContext.Response.WriteAsync(
            $"{context.HttpContext.Response.StatusCode} Error");
    });
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();

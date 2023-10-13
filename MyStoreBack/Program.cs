using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Seeder;
using MyStoreBack.Mapper;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddCors();

builder.Services.AddAutoMapper(typeof(MapperProfile));

builder.Services.AddDbContext<StoreDbContext>(opts =>
{
    opts.UseNpgsql(builder.Configuration.GetConnectionString("WebStoreConnection"));
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

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
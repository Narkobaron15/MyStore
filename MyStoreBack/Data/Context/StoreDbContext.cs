using Microsoft.EntityFrameworkCore;
using MyStoreBack.Data.Entity;

namespace MyStoreBack.Data.Context;

public class StoreDbContext : DbContext
{
    public StoreDbContext(DbContextOptions<StoreDbContext> opts) 
        : base(opts) {}

    public DbSet<CategoryEntity> Categories { get; set; } = null!;
}
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using MyStoreBack.Constants;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Entity;
using MyStoreBack.Data.Entity.Identity;

namespace MyStoreBack.Data.Seeder;

public static class DbSeeder
{
    public static async Task SeedDatabase(this IApplicationBuilder builder)
    {
        using var scope = builder.ApplicationServices.CreateScope();
        var ctx = scope.ServiceProvider.GetRequiredService<StoreDbContext>();
        var userManager = scope.ServiceProvider.GetRequiredService<UserManager<UserEntity>>();
        var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<RoleEntity>>();

        await MigrateIfAnyIn(ctx);

        if (!await ctx.Categories.AnyAsync())
            await SeedCategoriesInto(ctx);
        
        if (!await ctx.Roles.AnyAsync())
            await SeedRoles(roleManager);

        if (!await ctx.Users.AnyAsync())
            await SeedUsers(userManager);
    }

    private static async Task MigrateIfAnyIn(DbContext ctx)
    {
        if ((await ctx.Database.GetPendingMigrationsAsync()).Any())
            await ctx.Database.MigrateAsync();
    }

    private static async Task SeedCategoriesInto(DbContext ctx)
    {
        var categories = new List<CategoryEntity>
        {
            new(
                "Electronics",
                "https://c.dlnws.com/image/upload/c_lpad,dpr_auto,f_auto,h_240,q_auto:low,w_358/content/xklv1sg2urv0prxmodyb.jpg",
                "Explore the latest in electronic gadgets and devices."
            ),
            new(
                "Clothing",
                "https://d2fxao6r6rh9a0.cloudfront.net/media/catalog/product/cache/230da35529b8f597c7780d149b47f4f5/s/p/sp14bk.jpg",
                "Discover fashionable clothing for all occasions."
            ),
            new(
                "Books",
                "https://www.daysoftheyear.com/wp-content/uploads/world-book-day-scaled.jpg",
                "Dive into a world of books, from classics to bestsellers."
            ),
            new(
                "Home Decor",
                "https://images.ctfassets.net/5de70he6op10/2Ri6bD1TeMmWConq9ob9pH/a7ed1cd3039b77e99c5fcab65b88b844/Decor_Gateway_LS_01_b.jpg?w=1752&q=80&fm=jpg&fl=progressive",
                "Transform your living space with stylish home decor."
            )
        };

        foreach (var c in categories)
            await ctx.AddAsync(c);

        await ctx.SaveChangesAsync();
    }

    private static async Task SeedRoles(
        RoleManager<RoleEntity> roleManager)
    {
        var admin = new RoleEntity { Name = Roles.Admin };
        var user = new RoleEntity { Name = Roles.User };
        
        await roleManager.CreateAsync(admin);
        await roleManager.CreateAsync(user);
    }
    
    private static async Task SeedUsers(
        UserManager<UserEntity> userManager)
    {
        var admin = new UserEntity
        {
            UserName = "admin",
            Email = "admin@gmail.com",
            FirstName = "Marko",
            LastName = "Lysyi",
            EmailConfirmed = true
        };
        
        await userManager.CreateAsync(admin, "@Adminpwd123");
        await userManager.AddToRoleAsync(admin, Roles.Admin);
    }
}
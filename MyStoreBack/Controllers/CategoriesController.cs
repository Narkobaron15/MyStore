using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Entity;
using MyStoreBack.Models;

namespace MyStoreBack.Controllers;

[ApiController]
[Route("/api/[controller]")]
public class CategoriesController : ControllerBase
{
    private StoreDbContext Ctx { get; }
    private IMapper Mapper { get; }

    public CategoriesController(
        StoreDbContext ctx,
        IMapper mapper
    )
    {
        Ctx = ctx;
        Mapper = mapper;
    }

    [HttpGet]
    public async Task<IEnumerable<CategoryModel>> GetCategories()
    {
        var categories = await Ctx.Categories.ToListAsync();
        return Mapper.Map<IEnumerable<CategoryModel>>(categories);
    }

    [HttpGet("{id:int}")]
    public async Task<CategoryModel> GetCategoryById(int id)
    {
        var cat = await Ctx.Categories.FindAsync(id);
        return Mapper.Map<CategoryModel>(cat);
    }
}
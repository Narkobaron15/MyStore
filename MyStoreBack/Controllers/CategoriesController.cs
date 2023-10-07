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
    public async Task<IActionResult> GetCategories()
    {
        var categories = await Ctx.Categories.ToListAsync();
        var mapped = Mapper.Map<IEnumerable<CategoryModel>>(categories);
        return Ok(mapped);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetCategoryById(int id)
    {
        var cat = await Ctx.Categories.FindAsync(id);
        var mapped = Mapper.Map<CategoryModel>(cat);
        return Ok(mapped);
    }
}
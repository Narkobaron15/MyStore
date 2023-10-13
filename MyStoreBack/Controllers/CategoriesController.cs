using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MyStoreBack.Data.Context;
using MyStoreBack.Models;

namespace MyStoreBack.Controllers;

[ApiController]
[Route("/api/[controller]")]
public class CategoriesController : ControllerBase
{
    private StoreDbContext Context { get; }
    private IMapper Mapper { get; }

    public CategoriesController(
        StoreDbContext context,
        IMapper mapper
    )
    {
        Context = context;
        Mapper = mapper;
    }

    [HttpGet]
    public async Task<IActionResult> GetCategories()
    {
        var categories = await Context.Categories.ToListAsync();
        var mapped = Mapper.Map<IEnumerable<CategoryModel>>(categories);
        return Ok(mapped);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetCategoryById(int id)
    {
        var cat = await Context.Categories.FindAsync(id);
        var mapped = Mapper.Map<CategoryModel>(cat);
        return Ok(mapped);
    }
}
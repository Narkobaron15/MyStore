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
    public async Task<IActionResult> Get()
    {
        var categories = await Context.Categories.ToListAsync();
        var mapped = Mapper.Map<IEnumerable<CategoryModel>>(categories);
        return Ok(mapped);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        var cat = await Context.Categories.FindAsync(id);
        var mapped = Mapper.Map<CategoryModel>(cat);
        return Ok(mapped);
    }
    
    [HttpPost("create")]
    public async Task<IActionResult> Create([FromBody] CategoryCreateModel model)
    {
        try
        {
            var cat = Mapper.Map<CategoryEntity>(model);
            
            await Context.AddAsync(cat);
            await Context.SaveChangesAsync();
            
            return Ok(cat.Id);
        }
        catch
        {
            return BadRequest("Failed to create category.");
        }
    }
    
    [HttpPut("update")]
    public async Task<IActionResult> Update([FromBody] CategoryUpdateModel model)
    {
        try
        {
            var cat = await Context.Categories.FindAsync(model.Id) ?? throw new Exception();
            Mapper.Map(model, cat);
            
            Context.Update(cat);
            await Context.SaveChangesAsync();
            
            return Ok(cat.Id);
        }
        catch
        {
            return BadRequest("Failed to update category.");
        }            
    }
    
    [HttpDelete("delete/{id:int}")]
    public async Task<IActionResult> Delete(int id)
    {
        try
        {
            var cat = await Context.Categories.FindAsync(id) ?? throw new Exception();
            
            Context.Remove(cat);
            await Context.SaveChangesAsync();
            
            return Ok();
        }
        catch
        {
            return BadRequest("Failed to delete category.");
        }
    }
}
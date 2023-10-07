using MyStoreBack.Data.Entity;
using MyStoreBack.Models;

namespace MyStoreBack.Mapper;

public class MapperProfile : AutoMapper.Profile
{
    public MapperProfile()
    {
        CreateMap<CategoryEntity, CategoryModel>()
            .ForMember(x => x.Image, opt => opt.MapFrom(src => src.ImageUrl));

        CreateMap<CategoryModel, CategoryEntity>()
            .ForMember(x => x.ImageUrl, opt => opt.MapFrom(src => src.Image));
    }
}
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace MyStoreBack.Migrations
{
    public partial class ChangedImageUrlpropertyname : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "ImageURL",
                table: "tbl_categories",
                newName: "ImageUrl");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "ImageUrl",
                table: "tbl_categories",
                newName: "ImageURL");
        }
    }
}

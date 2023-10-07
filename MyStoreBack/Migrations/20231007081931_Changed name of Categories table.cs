using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace MyStoreBack.Migrations
{
    public partial class ChangednameofCategoriestable : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_Categories",
                table: "Categories");

            migrationBuilder.RenameTable(
                name: "Categories",
                newName: "tbl_categories");

            migrationBuilder.AddPrimaryKey(
                name: "PK_tbl_categories",
                table: "tbl_categories",
                column: "Id");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_tbl_categories",
                table: "tbl_categories");

            migrationBuilder.RenameTable(
                name: "tbl_categories",
                newName: "Categories");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Categories",
                table: "Categories",
                column: "Id");
        }
    }
}

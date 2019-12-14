functions = ["Sub"];
dir = get_absolute_file_path("loader.sce");
addinter(dir + "../../Native/x64/Debug/SciImager.dll", "SciImager", functions);

javacode = mgetl(dir + "../src/java/com/sciimager/SciImager.java");
jcompile("com.sciimager.SciImager", javacode);
jimport com.sciimager.SciImager;
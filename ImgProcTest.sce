RGB = imread ("VisualIpcvEditor/test.png");
RGB = resize_matrix(RGB, [size(RGB)(1), size(RGB)(2), 3])
Image = rgb2gray(RGB);
InvertedImage = uint8(255 * ones(size(Image,1), size(Image,2))) - Image;
Threshold=100;
level=Threshold/255;
LogicalImage = im2bw(InvertedImage, level);
StructureElement = imcreatese('rect',21,21);
FilteredLogicalImage = imclose(LogicalImage,StructureElement)
/*[ObjectImage,n] = imlabel(FilteredLogicalImage);

f1=scf(1);f1.name='Logical Image';
//imshow(LogicalImage);
f2=scf(2);f2.name='Filtered Logical Image';
//imshow(FilteredLogicalImage,jetcolormap(n))
f3=scf(3);f3.name='Result of Blob Analysis';
//imshow(ObjectImage,jetcolormap(n))

[Area, BB] = imblobprop(ObjectImage);
f4=scf(4);f4.name='Result of Blob Analysis';*/
imshow(FilteredLogicalImage,jetcolormap(n));
//imrects(BB,[255 0 0]);

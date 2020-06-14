C = zeros(3,200*200*3);
C(2, 1) = 200;
C(3, 1) = 200;
C2 = C;

printf('%d', C2(2, 1));
printf('%d', C2(3, 1));

for i=0:C(2.1)-1
	for j=0:C(3,1)-1
		index = (i * C2(3, 1) + j) * 3;
		ITER = 20;
		current = ITER;
		x = double(i);
		y = double(j);
		z = complex(0, 0);
		c = complex((x - double(C(2, 1)) / 2) / 50.0, (y - double(C(3, 1)) / 2) / 50.0);
		
		for k = 1:ITER
			z = z * z + c;
			r = real(z);
			img = imag(z);
			len = sqrt(r * r + img * img);

			if len > 16 then
				current = k;
				break;
			end;
		end;

		red = 0.1 + current / ITER * 0.2;
        green = 0.2 + current / ITER * 0.3;
        blue = 0.3 + current / ITER * 0.1;

		C2(1, index +1) = uint8(blue * 255);
		C2(1, index + 2) = uint8(green * 255);
		C2(1, index + 3) = uint8(red * 255);
	end;
end;

printf('Finish');

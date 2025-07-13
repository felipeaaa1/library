DO $$
BEGIN
	RAISE NOTICE 'validation to check roles and login column in usuario table';
	--login
	IF EXISTS (
		SELECT 1 FROM information_schema.columns
		WHERE column_name ILIKE 'login'
		  AND table_name ILIKE 'usuario'
	) THEN
		RAISE NOTICE 'login column already exists';
	ELSE
		ALTER TABLE usuario
        RENAME COLUMN nome TO login;

		RAISE NOTICE 'login column added with success';
	END IF;

	-- roles
	IF EXISTS (
		SELECT 1 FROM information_schema.columns
		WHERE column_name ILIKE 'roles'
		  AND table_name ILIKE 'usuario'
	) THEN
		RAISE NOTICE 'roles column already exists';
	ELSE
		ALTER TABLE usuario ADD roles VARCHAR[];
		UPDATE usuario SET roles = ARRAY['ROLE_DEFAULT'] WHERE roles IS NULL;
		ALTER TABLE usuario ALTER COLUMN roles SET NOT NULL;
        RAISE NOTICE 'roles column added with success';
	END IF;
END;
$$;

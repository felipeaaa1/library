DO $$
BEGIN
	RAISE NOTICE 'INITIALIZING MIGRATION TO ADD USER AND SERVER';

	CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

	-- Verifica se o usuário "gerente" já existe
	IF NOT EXISTS (
		SELECT 1 FROM usuario WHERE login = 'gerente' OR email = 'gerente@email.com'
	) THEN
		INSERT INTO usuario
		(id, login, senha, email, roles)
		VALUES
		(uuid_generate_v4(), 'gerente', '$2a$12$2yZZWHlYlKiNx8w3PLDpTO9U6VvsI7pp59hMSo4Upwx1q4Sdto46e', 'gerente@email.com', '{GERENTE}');
		RAISE NOTICE 'USER GERENTE INSERTED SUCCESSFULLY';
	ELSE
		RAISE NOTICE 'USER GERENTE ALREADY EXISTS';
	END IF;

	-- Verifica se o client "client-production" já existe
	IF NOT EXISTS (
		SELECT 1 FROM client WHERE client_id = 'client-production'
	) THEN
		INSERT INTO client
		(id, client_id, client_secret, redirect_uri, scope)
		VALUES
		(uuid_generate_v4(), 'client-production', '$2a$12$2yZZWHlYlKiNx8w3PLDpTO9U6VvsI7pp59hMSo4Upwx1q4Sdto46e', 'http://localhost:8080/authorized', 'GERENTE');
		RAISE NOTICE 'CLIENT client-production INSERTED SUCCESSFULLY';
	ELSE
		RAISE NOTICE 'CLIENT client-production ALREADY EXISTS';
	END IF;

END;
$$;

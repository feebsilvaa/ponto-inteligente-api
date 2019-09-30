INSERT INTO `tb_empresa` (`cnpj`, `data_atualizacao`, `data_criacao`, `razao_social`) 
VALUES ('75798614000160', CURRENT_DATE(), CURRENT_DATE(), 'FeeDev IT');

INSERT INTO `tb_funcionario` (`cpf`, `data_atualizacao`, `data_criacao`, `email`, `nome`, 
`perfil`, `qtd_horas_almoco`, `qtd_horas_trabalho_dia`, `senha`, `valor_hora`, `empresa_id`) 
VALUES ('16248890935', CURRENT_DATE(), CURRENT_DATE(), 'admin@feedev.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL, 
'$2a$06$xIvBeNRfS65L1N17I7JzgefzxEuLAL0Xk0wFAgIkoNqu9WD6rmp4m', NULL, 
(SELECT `id` FROM `tb_empresa` WHERE `cnpj` = '75798614000160'));
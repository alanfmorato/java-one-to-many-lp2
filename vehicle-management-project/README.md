# Vehicle Management REST API (No Frameworks)

Projeto Java (OpenJDK 17+) que implementa uma REST API sem uso de frameworks (Spring/Quarkus/Micronaut).
Este projeto foi gerado para cumprir as especificações do README fornecido.

## Como compilar

Requer: Java 17+ e Maven.

1. Gerar o JAR:
```bash
mvn -v
mvn clean package -DskipTests
```

2. Executar:
```bash
java -jar target/vehicle-management-1.0.0.jar
```

A aplicação inicia um servidor HTTP embutido na porta `8080`.

## Endpoints principais (JSON)
- `GET  /vehicles` - lista veículos
- `POST /vehicles` - cria veículo
- `GET  /vehicles/<built-in function id>` - obtém veículo
- `PUT  /vehicles/<built-in function id>` - atualiza veículo
- `DELETE /vehicles/<built-in function id>` - remove veículo

- `GET  /maintenance?vehicleId=<built-in function id>` - lista manutenções do veículo
- `POST /maintenance` - cria manutenção
- `GET  /maintenance/<built-in function id>` - obtém manutenção
- `PUT  /maintenance/<built-in function id>` - atualiza manutenção
- `DELETE /maintenance/<built-in function id>` - remove manutenção

## Regras implementadas (resumo)
- `vin` e `plate` únicos (validação em VehicleService).
- `year` validado (1900 ≤ year ≤ currentYear).
- `Maintenance.performedAt` não pode ser no futuro (para PerformedMaintenance).
- Veículos `DECOMMISSIONED` não aceitam novos registros de manutenção.
- Atualização de `Vehicle.currentMileage` quando `mileageAtService` excede o atual.

## Observações
- Banco de dados embutido H2 (arquivo `./data/vehicledb.mv.db`) — criado automaticamente.
- Projeto simples e didático; não possui autenticação.
- Código organizado em pacotes: entity, dto, repository, service, controller, exception, util.


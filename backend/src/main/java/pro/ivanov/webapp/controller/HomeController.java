package pro.ivanov.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.ivanov.webapp.model.Device;
import pro.ivanov.webapp.repository.DeviceRepository;
import pro.ivanov.webapp.responseModel.DeviceResponse;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final DeviceRepository deviceRepository;

    public HomeController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public ResponseEntity<?> index() {
        List<DeviceResponse> response = this.deviceRepository
                .findAll()
                .stream()
                .filter(device -> device.isActivated())
                .map(device -> DeviceResponse.of(device))
                .toList();

        return ResponseEntity.ok(response);
    }
}

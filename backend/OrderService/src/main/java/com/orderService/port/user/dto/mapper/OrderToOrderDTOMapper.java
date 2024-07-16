package com.orderService.port.user.dto.mapper;

import com.orderService.core.domain.model.*;
import com.orderService.port.user.dto.*;

import java.util.stream.Collectors;

public class OrderToOrderDTOMapper {

    public OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setDate(order.getDate());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setShippingAddress(convertToShippingAddressDTO(order.getShippingAddress()));
        orderDTO.setBillingAddress(convertToBillingAddressDTO(order.getBillingAddress()));
        orderDTO.setContactDetails(convertToContactDetailsDTO(order.getContactDetails()));
        orderDTO.setStatus(order.getStatus());
        orderDTO.setShipmentType(order.getShipmentType());
        orderDTO.setOrderItems(order.getOrderItems().stream().map(this::convertToOrderItemDTO).collect(Collectors.toList()));
        return orderDTO;
    }

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getSize(),
                orderItem.getColour(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    private ShippingAddressDTO convertToShippingAddressDTO(ShippingAddress shippingAddress) {
        if (shippingAddress == null) return null;
        return new ShippingAddressDTO(
                shippingAddress.getAddressId(),
                shippingAddress.getFirstName(),
                shippingAddress.getLastName(),
                shippingAddress.getStreet(),
                shippingAddress.getHouseNumber(),
                shippingAddress.getCity(),
                shippingAddress.getPostalCode(),
                shippingAddress.getCountry()
        );
    }

    private BillingAddressDTO convertToBillingAddressDTO(BillingAddress billingAddress) {
        if (billingAddress == null) return null;
        return new BillingAddressDTO(
                billingAddress.getAddressId(),
                billingAddress.getFirstName(),
                billingAddress.getLastName(),
                billingAddress.getStreet(),
                billingAddress.getHouseNumber(),
                billingAddress.getCity(),
                billingAddress.getPostalCode(),
                billingAddress.getCountry()
        );
    }

    private ContactDetailsDTO convertToContactDetailsDTO(ContactDetails contactDetails) {
        if (contactDetails == null) return null;
        return new ContactDetailsDTO(
                contactDetails.getContactEmail(),
                contactDetails.getContactPhone()
        );
    }
}


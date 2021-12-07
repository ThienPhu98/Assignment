package banking.management.controller;

import banking.management.model.Customer;
import banking.management.model.Deposit;
import banking.management.model.Transfer;
import banking.management.model.Withdraw;
import banking.management.service.ICustomerService;
import banking.management.service.IDepositService;
import banking.management.service.ITransferService;
import banking.management.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class ProcessingController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private IWithdrawService withdrawService;

    @GetMapping("")
    public ModelAndView showCustomerList() {
        ModelAndView modelAndView = new ModelAndView("/customerList");
        Iterable<Customer> customerList = customerService.findAll();
        List<Customer> customerValidList = new ArrayList<>();
        for (Customer customerObj : customerList) {
            if (customerObj.getDeleted() == (byte) 0) {
                customerValidList.add(customerObj);
            }
        }
        modelAndView.addObject("customerList", customerValidList);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createCustomer(@Validated @ModelAttribute("customer") Customer customer, BindingResult bindingResult) {
        ModelAndView modelAndView = null;
        if (bindingResult.hasFieldErrors()) {
            modelAndView = new ModelAndView("/create");
            modelAndView.addObject("message", "create customer fail! please follow instructor!");
        } else {
            customer.setCreated_by(1);
            customer.setCreated_at(String.valueOf(java.time.LocalDateTime.now()));
            customer.setBalance(BigDecimal.valueOf(0));
            customerService.save(customer);
            modelAndView = new ModelAndView("/customerList");
            Iterable<Customer> customerList = customerService.findAll();
            modelAndView.addObject("customerList", customerList);
            modelAndView.addObject("message", "create customer success!");
        }
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customerEdit = customerService.findById(id);
        if (customerEdit.isPresent()) {
            modelAndView = new ModelAndView("/edit");
            modelAndView.addObject("customerEdit", customerEdit.get());
            modelAndView.addObject("customer", new Customer());
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editCustomerInformation(@PathVariable long id, @Validated @ModelAttribute("customer") Customer customer, BindingResult bindingResult) {
        ModelAndView modelAndView = null;
        if (bindingResult.hasFieldErrors()) {
            modelAndView = new ModelAndView("/edit");
            modelAndView.addObject("message", "create customer fail! please follow instructor!");
        } else {
            Optional<Customer> customerOptional = customerService.findById(id);
            if (customerOptional.isPresent()) {
                modelAndView = new ModelAndView("/customerList");
                Customer customerEdit = customerOptional.get();
                customerEdit.setAddress(customer.getAddress());
                customerEdit.setFull_name(customer.getFull_name());
                customerEdit.setEmail(customer.getEmail());
                customerEdit.setPhone(customer.getPhone());
                customerService.save(customerEdit);
                Iterable<Customer> customerList = customerService.findAll();
                modelAndView.addObject("customerList", customerList);
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        }
        return modelAndView;
    }

    @GetMapping("/{id}/deposit")
    public ModelAndView showDeposit(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            modelAndView = new ModelAndView("/deposit");
            modelAndView.addObject("customer", customer.get());
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/{id}/deposit")
    public ModelAndView deposit(@PathVariable long id, @ModelAttribute("amount") String amount) {
        ModelAndView modelAndView = null;
        if (amount.length() > 12) {
            Optional<Customer> customerOptional = customerService.findById(id);
            if (customerOptional.isPresent()) {
                modelAndView = new ModelAndView("/deposit");
                modelAndView.addObject("customer", customerOptional.get());
                modelAndView.addObject("fail", "Deposit fail! Amount money to deposit must be less than 12 element!");
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        } else {
            BigDecimal amountDeposit = BigDecimal.valueOf(Long.parseLong(amount));

            Optional<Customer> customer = customerService.findById(id);
            if (customer.isPresent()) {
                Customer customerDeposit = customer.get();
                customerDeposit.setBalance(customerDeposit.getBalance().add(amountDeposit));
                customerService.save(customerDeposit);

                Deposit deposit = new Deposit();
                deposit.setCreated_at(String.valueOf(java.time.LocalDateTime.now()));
                deposit.setCustomer(customerDeposit);
                deposit.setCreated_by(0);
                depositService.save(deposit);

                Optional<Customer> customerOptional = customerService.findById(id);
                if (customerOptional.isPresent()) {
                    modelAndView = new ModelAndView("/deposit");
                    modelAndView.addObject("customer", customerOptional.get());
                    modelAndView.addObject("success", "Deposit success!");
                } else {
                    modelAndView = new ModelAndView("/error.404");
                }
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        }



        return modelAndView;
    }

    @GetMapping("/{id}/withdraw")
    public ModelAndView showWithdraw(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            modelAndView = new ModelAndView("/withdraw");
            modelAndView.addObject("customer", customer.get());
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/{id}/withdraw")
    public ModelAndView withdraw(@PathVariable long id, @ModelAttribute("amount") String amount) {
        ModelAndView modelAndView = null;

        if (amount.length() > 12) {
            Optional<Customer> customerOptional = customerService.findById(id);
            if (customerOptional.isPresent()) {
                modelAndView = new ModelAndView("/withdraw");
                modelAndView.addObject("customer", customerOptional.get());
                modelAndView.addObject("fail", "Withdraw Fail! Amount money to withdraw must be less than 12 element!");
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        } else {
            BigDecimal amountWithdraw = BigDecimal.valueOf(Long.parseLong(amount));

            Optional<Customer> customer = customerService.findById(id);
            if (customer.isPresent()) {
                Customer customerWithdraw = customer.get();
                if (customerWithdraw.getBalance().compareTo(amountWithdraw) >= 0) {
                    customerWithdraw.setBalance(customerWithdraw.getBalance().subtract(amountWithdraw));
                    customerService.save(customerWithdraw);

                    Withdraw withdraw = new Withdraw();
                    withdraw.setCreated_at(String.valueOf(java.time.LocalDateTime.now()));
                    withdraw.setCustomer(customerWithdraw);
                    withdraw.setCreated_by(0);
                    withdrawService.save(withdraw);

                    Optional<Customer> customerOptional = customerService.findById(id);
                    if (customerOptional.isPresent()) {
                        modelAndView = new ModelAndView("/withdraw");
                        modelAndView.addObject("customer", customerOptional.get());
                        modelAndView.addObject("success", "Withdraw success!");
                    } else {
                        modelAndView = new ModelAndView("/error.404");
                    }
                } else {
                    Optional<Customer> customerOptional = customerService.findById(id);
                    if (customerOptional.isPresent()) {
                        modelAndView = new ModelAndView("/withdraw");
                        modelAndView.addObject("customer", customerOptional.get());
                        modelAndView.addObject("fail", "Withdraw Fail! Amount money to withdraw must be less than your balance!");
                    } else {
                        modelAndView = new ModelAndView("/error.404");
                    }
                }
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        }

        return modelAndView;
    }

    @GetMapping("/{id}/transfer")
    public ModelAndView showTransfer(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            modelAndView = new ModelAndView("/transfer");
            Iterable<Customer> customerList = customerService.findAll();
            List<Customer> customerValidList = new ArrayList<>();
            for (Customer customerObj : customerList) {
                if (customerObj.getDeleted() == (byte) 0 && customerObj.getId() != id) {
                    customerValidList.add(customerObj);
                }
            }
            modelAndView.addObject("customer", customer.get());
            modelAndView.addObject("customerValidList", customerValidList);
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/{id}/transfer")
    public ModelAndView transfer(@PathVariable long id, @ModelAttribute("amount") String amount,
                                 @ModelAttribute("fee") int fee, @ModelAttribute("recipientId") long recipientId) {
        ModelAndView modelAndView = null;
        Optional<Customer> senderOptional = customerService.findById(id);
        if (senderOptional.isPresent()) {
            Optional<Customer> recipientOptional = customerService.findById(recipientId);
            if (recipientOptional.isPresent()) {
                if (amount.length() > 12) {
                    modelAndView = new ModelAndView("/transfer");
                    Iterable<Customer> customerList = customerService.findAll();
                    List<Customer> customerValidList = new ArrayList<>();
                    for (Customer customerObj : customerList) {
                        if (customerObj.getDeleted() == (byte) 0 && customerObj.getId() != id) {
                            customerValidList.add(customerObj);
                        }
                    }
                    modelAndView.addObject("customer", senderOptional.get());
                    modelAndView.addObject("customerValidList", customerValidList);
                    modelAndView.addObject("fail", "transfer fail! Amount money to transfer must be less than 12 element!");
                } else {
                    Customer sender = senderOptional.get();
                    Customer recipient = recipientOptional.get();
                    BigDecimal amountTransfer = BigDecimal.valueOf(Long.parseLong(amount) * 1.1);
                    BigDecimal amountReceive = BigDecimal.valueOf(Long.parseLong(amount));
                    if (sender.getBalance().compareTo(amountTransfer) >= 0) {
                        sender.setBalance(sender.getBalance().subtract(amountTransfer));
                        customerService.save(sender);

                        recipient.setBalance(recipient.getBalance().add(amountReceive));
                        customerService.save(recipient);

                        Transfer transfer = new Transfer();
                        transfer.setCreated_by(0);
                        transfer.setCreated_at(String.valueOf(java.time.LocalDateTime.now()));
                        transfer.setFees(fee);
                        transfer.setFees_amount(amountTransfer.subtract(amountReceive));
                        transfer.setTransfer_amount(amountTransfer);
                        transfer.setTransaction_amount(amountReceive);
                        transfer.setRecipient(recipient);
                        transfer.setSender(sender);
                        transferService.save(transfer);

                        modelAndView = new ModelAndView("/customerList");
                        Iterable<Customer> customerList = customerService.findAll();
                        List<Customer> customerValidList = new ArrayList<>();
                        for (Customer customerObj : customerList) {
                            if (customerObj.getDeleted() == (byte) 0) {
                                customerValidList.add(customerObj);
                            }
                        }
                        modelAndView.addObject("customerList", customerValidList);
                        modelAndView.addObject("message", "transfer success");
                    } else {
                        modelAndView = new ModelAndView("/transfer");
                        Iterable<Customer> customerList = customerService.findAll();
                        List<Customer> customerValidList = new ArrayList<>();
                        for (Customer customerObj : customerList) {
                            if (customerObj.getDeleted() == (byte) 0 && customerObj.getId() != id) {
                                customerValidList.add(customerObj);
                            }
                        }
                        modelAndView.addObject("customer", senderOptional.get());
                        modelAndView.addObject("customerValidList", customerValidList);
                        modelAndView.addObject("fail", "transfer fail! Amount money to transfer must be equal or less than sender's balance ");
                    }
                }
            } else {
                modelAndView = new ModelAndView("/error.404");
            }
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @GetMapping("/{id}/delete")
    public ModelAndView showDelete(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            modelAndView = new ModelAndView("/delete");
            modelAndView.addObject("customer", customer.get());
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView modelAndView = null;
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            Customer customerDelete = customer.get();
            customerDelete.setDeleted((byte) 1);
            customerDelete.setUpdated_by(0);
            customerDelete.setUpdated_at(String.valueOf(java.time.LocalDateTime.now()));
            customerService.save(customerDelete);
            modelAndView = new ModelAndView("/customerList");
            Iterable<Customer> customerList = customerService.findAll();
            List<Customer> customerValidList = new ArrayList<>();
            for (Customer customerObj : customerList) {
                if (customerObj.getDeleted() == (byte) 0) {
                    customerValidList.add(customerObj);
                }
            }
            modelAndView.addObject("customerList", customerValidList);
            modelAndView.addObject("message", "delete customer success");
        } else {
            modelAndView = new ModelAndView("/error.404");
        }
        return modelAndView;
    }
}

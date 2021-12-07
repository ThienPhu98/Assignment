package banking.management.controller;

import banking.management.model.Customer;
import banking.management.model.Transfer;
import banking.management.service.ICustomerService;
import banking.management.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class ProcessingController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

    @GetMapping("")
    public ModelAndView showList() {
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        List<Customer> customerArrayList = customerService.findAll();
        modelAndView.addObject("customerList", customerArrayList);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        List<Customer> customerArrayList = customerService.findAll();
        modelAndView.addObject("customerList", customerArrayList);
        modelAndView.addObject("message", "New customer created successfully");
        return modelAndView;
    }

    @GetMapping("/{id}/deposit")
    public ModelAndView showDeposit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customer/deposit");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/deposit")
    public ModelAndView deposit(@ModelAttribute("id") long id, @ModelAttribute("amount") double amount) {
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        Customer customer = customerService.findById(id);
        double balance = customer.getBalance();
        customer.setBalance(balance + amount);
        customerService.save(customer);
        List<Customer> customerArrayList = customerService.findAll();
        modelAndView.addObject("customerList", customerArrayList);
        modelAndView.addObject("message", "Deposit successfully");
        return modelAndView;
    }

    @GetMapping("/{id}/withdraw")
    public ModelAndView showWithdraw(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customer/withdraw");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/withdraw")
    public ModelAndView withdraw(@ModelAttribute("id") long id, @ModelAttribute("amount") double amount) {
        ModelAndView modelAndView = null;
        Customer customer = customerService.findById(id);
        if (customer.getBalance() < amount) {
            modelAndView = new ModelAndView("/customer/withdraw");
            modelAndView.addObject("customer", customer);
            modelAndView.addObject("message", "Withdraw fail! Amount money to withdraw must be less than your balance!");
        } else {
            modelAndView = new ModelAndView("/customer/list");
            customer.setBalance(customer.getBalance() - amount);
            customerService.save(customer);
            List<Customer> customerArrayList = customerService.findAll();
            modelAndView.addObject("customerList", customerArrayList);
            modelAndView.addObject("message", "Withdraw successfully");
        }
        return modelAndView;
    }

    @GetMapping("/{id}/transfer")
    public ModelAndView showTransfer(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customer/transfer");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        List<Customer> customerList = customerService.findAll();
        List<Customer> receiverList = new ArrayList<>();
        for(Customer customerObj : customerList) {
            if (customerObj.getId() != id) {
                receiverList.add(customerObj);
            }
        }
        modelAndView.addObject("receiverList", receiverList);
        return modelAndView;
    }

    @PostMapping("/transfer")
    public ModelAndView transfer(@ModelAttribute("depositorId") long depositorId, @ModelAttribute("receiverId") long receiverId, @ModelAttribute("amount") double amount){
        ModelAndView modelAndView = null;
        Customer depositor = customerService.findById(depositorId);
        Customer receiver = customerService.findById(receiverId);
        double totalAmountTransfer = (amount * 1.1);

        if (depositor.getBalance() < totalAmountTransfer) {
            modelAndView = new ModelAndView("/customer/transfer");
            List<Customer> customerList = customerService.findAll();
            List<Customer> receiverList = new ArrayList<>();
            for(Customer customerObj : customerList) {
                if (customerObj.getId() != depositorId) {
                    receiverList.add(customerObj);
                }
            }
            modelAndView.addObject("receiverList", receiverList);
            modelAndView.addObject("customer", depositor);
            modelAndView.addObject("message", "Transfer fail! Amount money to transfer (plus 10%) must be less than your balance!");
        } else {
            modelAndView = new ModelAndView("/customer/list");
            depositor.setBalance(depositor.getBalance() - totalAmountTransfer);
            receiver.setBalance(receiver.getBalance() + amount);
            customerService.save(depositor);
            customerService.save(receiver);

            double interested = (amount * 0.1);
            Transfer transfer = new Transfer(amount, depositorId, receiverId, interested);
            transferService.save(transfer);

            List<Customer> customerArrayList = customerService.findAll();
            modelAndView.addObject("customerList", customerArrayList);
            modelAndView.addObject("message", "Transfer successfully");
        }

        return modelAndView;
    }

    @GetMapping("/{id}/delete")
    public ModelAndView showDelete(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/customer/delete");
        Customer customer = customerService.findById(id);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView delete(@ModelAttribute("id") long id) {
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        customerService.remove(id);
        List<Customer> customerArrayList = customerService.findAll();
        modelAndView.addObject("customerList", customerArrayList);
        modelAndView.addObject("message", "Delete customer successfully");
        return modelAndView;
    }
}

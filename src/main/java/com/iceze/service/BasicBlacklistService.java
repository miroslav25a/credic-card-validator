package com.iceze.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.iceze.model.BlacklistDTO;
import com.iceze.model.CreditCard;

/**
 * This class contains the implementation of the basic blacklist service.
 * 
 * @author miroslav
 */
@Service("blacklistService")
public class BasicBlacklistService implements BlacklistService {
	private String filePath;
	private BlacklistDTO blacklistDto;
	
	public BasicBlacklistService(@Value("${blacklist.file.path}") final String filePath) throws IOException {
		this.filePath = filePath;
		String json = new String(Files.readAllBytes(Paths.get(this.filePath)));
        Gson gson = new Gson();
        BlacklistDTO bl = gson.fromJson(json, BlacklistDTO.class);
        
        List<String> blacklistNumbers = bl.getBlacklist().stream()
        												 .map(s -> { 
        													 	return s.trim().replaceAll("\\s", "");
        												 	 })
        												 .collect(Collectors.toList());
        this.blacklistDto = new BlacklistDTO(blacklistNumbers);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlacklisted(CreditCard card) {
		return this.blacklistDto.getBlacklist().contains(card.getNumber()
															 .replaceAll("\\s", "")
															 .trim());
	}
}

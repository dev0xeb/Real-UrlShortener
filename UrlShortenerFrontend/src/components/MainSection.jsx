import React, { useState, useEffect } from 'react';
import logo from '../assets/logo.png';
import axios from 'axios';

const professions=[
    'Youtubers.',
    'Influencers.',
    'Marketers.',
    'Developers.',
    'Bloggers.',
    'E-commerce.',
    'Entrepreneurs.',
    'Educators.',
    'and much more.'
]

const MainSection = () => {
    const [currentProfession, setCurrentProfession] = useState(professions[0]);
    const [shortUrl, setShortUrl] = useState('')
    const [inputValue, setInputValue] = useState('')

    useEffect(() =>{
        const interval = setInterval(() =>{
            setCurrentProfession(prevProfession =>{
                const currentIndex = professions.indexOf(prevProfession);
                const newIndex = (currentIndex + 1) % professions.length;
                return professions[newIndex];
            });
        }, 3000);
        return () => clearInterval(interval);
    }, [])

    const handleShortUrl = async () => {
        try{
            const response = await axios.post('http://localhost:8080/api/shorten', {longUrl: inputValue});
            setShortUrl(response.data.shortUrl);
        } catch (error) {
            console.error('Error generating short URL:', error);
        }
    }

    const handleInputChange = (event) =>{
        setInputValue(event.target.value);
    }

    const handleCopyToClipboard =() =>{
        navigator.clipboard.writeText(shortUrl).then(() => {
            alert('Copied to clipboard:', shortUrl);
            }, (error) => {
                console.error('Error copying to clipboard:', error);
    })
    };
  return (
    <div className="main-section">
        <img src={logo} alt=""  className='mb-4'/>
        <p className='intro'>A very Useful Tool for <u className='profession'>{currentProfession}</u></p>
        <input 
            type="text" 
            placeholder='Input link here'
            value={inputValue}
            onChange={handleInputChange}
            />
        <button onClick={handleShortUrl}>Shorten</button>      
  
    {
        shortUrl && (
            <div className='clipboard'>
            <p> Shortened URL: <a href={shortUrl} target='_blank' rel='noopener noreferrer'>{shortUrl}</a></p>
            <button onClick={handleCopyToClipboard}>Copy</button>
            </div>
        )}
    </div>
  )
}

export default MainSection

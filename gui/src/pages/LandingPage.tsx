import React, { useRef } from 'react';
import Navbar from '../components/landingPage/Navbar';
import HeroSection from '../components/landingPage/HeroSection';
import PerksSection from '../components/landingPage/PerksSection';
import FeaturesSection from '../components/landingPage/FeaturesSection';
import PlanSection from '../components/landingPage/PlanSection';
import Footer from '../components/landingPage/Footer';
import { Theme, useTheme } from '@mui/material';

const LandingPage = () => {
  const theme: Theme = useTheme();
  const perksSectionRef = useRef<HTMLDivElement | null>(null);
  const planSectionRef = useRef<HTMLDivElement | null>(null);
  const featuresSectionRef = useRef<HTMLDivElement | null>(null);
  const contactSectionRef = useRef<HTMLDivElement | null>(null);

  const scrollToSection = (sectionRef: React.RefObject<HTMLDivElement>) => {
    if (sectionRef.current) {
      sectionRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <div>
      <Navbar
        theme={theme}
        scrollToPerksSection={() => scrollToSection(perksSectionRef)}
        scrollToFeaturesSection={() => scrollToSection(featuresSectionRef)}
        scrollToPlanSection={() => scrollToSection(planSectionRef)}
        scrollToContactSection={() => scrollToSection(contactSectionRef)}
      />
      <HeroSection theme={theme} scrollToPlanSection={() => scrollToSection(planSectionRef)} />
      <PerksSection ref={perksSectionRef} theme={theme} />
      <FeaturesSection ref={featuresSectionRef} theme={theme} />
      <PlanSection ref={planSectionRef} theme={theme} />
      <Footer ref={contactSectionRef} theme={theme} />
    </div>
  );
};

export default LandingPage;

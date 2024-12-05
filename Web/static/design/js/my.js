

// Curser Pointer

let cursor = document.querySelector(".cursor");
let cursor2 = document.querySelector(".cursor2");
let cursorScale = document.querySelectorAll(".cursor-scale");
let mouseX = 0;
let mouseY = 0;

gsap.to({}, 0.016, {
  repeat: -1,
  onRepeat: function () {
    gsap.set(cursor, {
      css: {
        left: mouseX,
        top: mouseY,
      },
    });
    gsap.set(cursor2, {
      css: {
        left: mouseX,
        top: mouseY,
      },
    });
  },
});

// Mouse Pointer

window.addEventListener("mousemove", (e) => {
  mouseX = e.clientX;
  mouseY = e.clientY;
});

cursorScale.forEach((link) => {
  link.addEventListener("mousemove", () => {
    cursor.classList.add("grow");
    if (link.classList.contains("small")) {
      cursor.classList.remove("grow");
      cursor.classList.add("grow-small");
    }
  });

  link.addEventListener("mouseleave", () => {
    cursor.classList.remove("grow");
    cursor.classList.remove("grow-small");
  });
});

// scroll up

let scrollPercentage = () => {
  let scrollProgress = document.getElementById("progress");
  let progressValue = document.getElementById("progress-value");
  let pos = document.documentElement.scrollTop;
  let calcHeight =
    document.documentElement.scrollHeight -
    document.documentElement.clientHeight;
  let scrollValue = Math.round((pos * 100) / calcHeight);

  scrollProgress.style.background = `conic-gradient(#01ADEF ${scrollValue}%, #c0c0ff ${scrollValue}%)`;
  progressValue.textContent = `${scrollValue}%`;

  if (pos > 20) {
    scrollProgress.classList.remove("hide");
    scrollProgress.classList.add("show");
  } else {
    scrollProgress.classList.remove("show");
    scrollProgress.classList.add("hide");
  }

  scrollProgress.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
};

window.onscroll = scrollPercentage;
window.onload = scrollPercentage;

/*======================================
	27. Smooth Scroll
	========================================*/
gsap.registerPlugin(ScrollTrigger, ScrollSmoother);

//Comment me out to see issue
const smoother = ScrollSmoother.create({
  wrapper: "#smooth-wrapper",
  content: "#smooth-content",
  smooth: 1.2,
  normalizeScroll: true,
  ignoreMobileResize: true,
  effects: true,
  preventDefault: true,
});

//Horizontal Scroll Galleries
if (document.getElementById("portfolio")) {
  const horizontalSections = gsap.utils.toArray(".horiz-gallery-wrapper");

  horizontalSections.forEach(function (sec, i) {
    const pinWrap = sec.querySelector(".horiz-gallery-strip");

    let pinWrapWidth;
    let horizontalScrollLength;

    function refresh() {
      pinWrapWidth = pinWrap.scrollWidth;
      horizontalScrollLength = pinWrapWidth - window.innerWidth;
    }

    // window.addEventListener("load", function () {
    refresh();
    // Pinning and horizontal scrolling
    let scrollTween = gsap.to(pinWrap, {
      scrollTrigger: {
        scrub: true,
        trigger: sec,
        pin: sec,
        start: "center center",
        end: () => `+=${pinWrapWidth}`,
        invalidateOnRefresh: true,
      },
      x: () => -horizontalScrollLength,
      ease: "none",
    });

    pinWrap.querySelectorAll("[data-speed-x]").forEach((el, i) => {
      let speed = parseFloat(el.getAttribute("data-speed-x"));
      gsap.to(el, {
        x: () => (1 - speed) * (window.innerWidth + el.offsetWidth),
        ease: "none",
        scrollTrigger: {
          containerAnimation: scrollTween,
          trigger: el,
          onRefresh: (self) => {
            let start = Math.max(0, self.start);
            self.setPositions(
              start,
              start + (self.end - self.start) / Math.abs(speed)
            ); // adjust for how long it'll stay in view
            self.animation.progress(0);
          },
          scrub: true,
        },
      });
    });

    ScrollTrigger.addEventListener("refreshInit", refresh);
    // });
  });
}

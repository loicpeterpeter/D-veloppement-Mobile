//
//  Raquette.swift
//  CasseBrique
//


import UIKit

class Raquette: UIView {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder : aDecoder)
    }
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        if let touch = touches.first {
            let position = touch.location(in: self)
            paddle.px = position.x - (paddle.sx * vX)/2
        }
        self.setNeedsDisplay()

    }
    
    var timer = Timer()
    

    
    var ball = Ball(Sx : 10,Sy : 10,Px: 200,Py : 500)
    var life = 3
    var score = 0
    var level = 0
    var bricks = [Brick]()
    var brickLeft = 0
    var vX = CGFloat (500)
    var playing = true
    var time = Float(5)
    func scheduledTimerWithTimeInterval(){
        // Scheduling timer to Call the function "updateCounting" with the interval of 1 seconds

        timer = Timer.scheduledTimer(timeInterval: 0.02, target: self, selector: #selector(self.update), userInfo: nil, repeats: true)
    }
    
    func Reset(){
        brickLeft = 0
        life = 3
        level = 0
        score = 0
        bricks = [Brick]()
        playing = true
        time = Float(5)
    }
    //var vX : CGFloat
    //var vY : CGFloat
    func Load(){
    }

    var paddle = Raquette2(Sx : 0.5 ,Sy : 0.02,Px: 0,Py : 0.75)
    
    override init(frame : CGRect){


        super.init(frame: frame);
        self.becomeFirstResponder()

        backgroundColor = UIColor.clear;
        scheduledTimerWithTimeInterval();
        
    }

    func generate(x : CGFloat , y : CGFloat){
        let X = x-1
        let Y = y-1
        for i in 1...(Int(x)) {
            for j in 1...(Int(y)){
                bricks.append(Brick(Sx : vX/x , Sy : (vX/x)/2 , Px : ((vX/x)) * CGFloat(i-1) , Py : ((vX/x)/2) * CGFloat(j-1)  ))
                brickLeft = brickLeft + 1;
            }
        }
    }
    
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        
        if(playing){
        let context = UIGraphicsGetCurrentContext()

        var vX = self.frame.width
        let vY = self.frame.height
        context?.setLineWidth(5)
        UIColor.gray.setStroke()
        if(ball.px<0 || ball.px + ball.sx > vX){
            ball.sideBounce()
        }
        if(ball.py < 0){
            ball.upBounce()
        }
        let carre2 = CGRect(x:ball.px , y:ball.py , width:ball.sx , height:ball.sy)
        var i = 0
        for brick in bricks{
      
            let carre3 = CGRect(x:brick.px , y:brick.py , width:brick.sx , height:brick.sy)

            if(carre3.intersects(carre2)){
                
                bricks.remove(at : i)
                ball.paddleBounce()
                brickLeft -= 1
                score = score + 10
                break
            }
                  i = i+1
        }
        for brick in bricks{
            let carre3 = CGRect(x:brick.px , y:brick.py , width:brick.sx , height:brick.sy)
            context?.addRect(carre3)

        }
        let carre = CGRect(x:paddle.px  , y:paddle.py * vY  , width:paddle.sx * vX, height:paddle.sy * vY )
        if (bricks.count == 0){
            switch(level){
            case 0:
            generate(x : 1,y : 3)
                level += 1
                break
            case 1:
            generate(x : 5,y : 4)
                level += 1
            case 2:
            generate(x : 10,y : 4)
                level += 1
            default:
                break
            }
            
        }
        if(ball.py > carre.minY + carre.height ){
            life -= 1
            ball.px = 250
            ball.py = 250
        }
            if(life <= 0){
                playing = false
            }
        if carre.intersects(carre2){
            ball.paddleBounce()
        }
        ball.move()

        context?.addRect(carre)
        context?.addRect(carre2)

        context?.strokePath()
        

        // Drawing code
    }
        else{
            if(time>0){
                time = time - Float(0.02)
            }
            else{
                Reset()
            }
        }
    }

    @objc func update(){
        self.setNeedsDisplay()

    }
    override func motionBegan(_ motion: UIEventSubtype , with event: UIEvent?){
    self.setNeedsDisplay()
    }
    
    override var canBecomeFirstResponder: Bool {
        get {
            return true
        }
    }
    
    
    class Brick{
        var px , py , sx , sy : CGFloat
        
        init(Sx : CGFloat , Sy : CGFloat , Px : CGFloat , Py : CGFloat ){
            sx = Sx
            sy = Sy
            px = Px
            py = Py
            
        }
    }
    class Ball {
        var px,py,sx,sy , vx , vy : CGFloat

        func move(){
            px = px + vx
            py = py + vy
        }
        
        init(Sx : CGFloat , Sy : CGFloat , Px : CGFloat , Py : CGFloat ){
            sx = Sx
            sy = Sy
            px = Px
            py = Py
            vx = -5
            vy = -5
            
        }
        func paddleBounce(){
            vy = -vy
        }
        
        func sideBounce(){
            vx =  -vx
        }
        func upBounce(){
            vy = -vy
        }
    }

    class Raquette2 {
        var px,py,sx,sy : CGFloat
        
        
        
        
        init(Sx : CGFloat , Sy : CGFloat , Px : CGFloat , Py : CGFloat ){
            sx = Sx
            sy = Sy
            px = Px
            py = Py
            
        }
        
        func move(Px : CGFloat , Py : CGFloat){
            px = Px
            py = Py
        }
        
        
        
        
        /*
         // Only override draw() if you perform custom drawing.
         // An empty implementation adversely affects performance during animation.
         override func draw(_ rect: CGRect) {
         // Drawing code
         }
         */
        
    }
}

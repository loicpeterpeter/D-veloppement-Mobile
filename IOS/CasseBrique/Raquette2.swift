//
//  Raquette2.swift
//  CasseBrique
//



class Raquette2 {
    var px,py,sx,sy : Float	
    
    
    
    
    init(Sx : Float , Sy : Float , Px : Float , Py : Float ){
         sx = Sx
         sy = Sy
         px = Px
         py = Py
        
    }
    
    func move(Px : Float , Py : Float){
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

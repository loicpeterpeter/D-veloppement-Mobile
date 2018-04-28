//
//  ViewController.swift
//  CasseBrique
//

import UIKit

class ViewController: UIViewController {
    @IBOutlet weak var test2: UIView!
    @IBOutlet var ballLeftView : UITextView!
    @IBOutlet var levelView : UITextView!
    @IBOutlet var scoreView : UITextView!
    @IBOutlet var resultView : UITextView!
    let test = Raquette(frame: CGRect(x:0,y:0 , width:UIScreen.main.bounds.width , height:UIScreen.main.bounds.height));

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.

        scoreView.text = "Score" + String (test.score);
        ballLeftView.text = "Life : " + String (test.life);
        levelView.text = "Level " + String (test.level);
        if(test.playing){
            resultView.text = ""
        }
        if(test.playing == false){
            resultView.text = "Game Over.Restarting now"
        }
        print(test.playing)
        test2.addSubview(test)
        var timer = Timer.scheduledTimer(timeInterval: 0.1, target: self, selector: #selector(self.update), userInfo: nil, repeats: true)

        
        


        
        
    }
    
    @objc func update () {
        scoreView.text = "Score" + String (test.score);
        ballLeftView.text = "Life : " + String (test.life);
        levelView.text = "Level " + String (test.level);
        if(test.playing){
            resultView.text = ""
        }
        if(test.playing == false){
            resultView.text = "Game Over.Reseting"
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
        // Dispose of any resources that can be recreated.
    }


}


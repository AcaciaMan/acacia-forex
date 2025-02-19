import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';
import M_Config from '../m_config';
import {PythonMessage} from '../python_message';
import { M_Logging } from '../m_logging';

export class PredictionsProvider implements vscode.WebviewViewProvider {
    public static readonly viewType = 'acacia-forex.predictions';

    constructor(private context: vscode.ExtensionContext) {}

    public resolveWebviewView(
        webviewView: vscode.WebviewView,
        context: vscode.WebviewViewResolveContext,
        _token: vscode.CancellationToken
    ): void {
        webviewView.webview.options = {
            enableScripts: true
        };

        webviewView.webview.html = this._getHtmlForWebview(webviewView.webview);

        webviewView.webview.onDidReceiveMessage(
            async message => {
                switch (message.command) {
                    case 'showPredictions':
                        vscode.window.showInformationMessage('Predictions button clicked');
                        let m_python_message = new PythonMessage( "JSONReader().__init__()", {"something": 1} );
                        await M_Config.main_con.send( m_python_message );

                        m_python_message = new PythonMessage( "EnvTrends().__init__()", {"something": 1} );
                        await M_Config.main_con.send( m_python_message );

                        m_python_message = new PythonMessage( "KeewDecomp().__init__()", {"something": 1} );
                        await M_Config.main_con.send( m_python_message );

                        const workspaceFolder = vscode.workspace.workspaceFolders ? vscode.workspace.workspaceFolders[0].uri.fsPath : '';
                        m_python_message = new PythonMessage( "m_predictions.load_fx_data()", {"file_path": workspaceFolder + "/filtered_data.json"} );
                        await M_Config.main_con.send( m_python_message );
                        
                        m_python_message = new PythonMessage( "m_predictions.create_folder_structure()", {"dir": workspaceFolder} );
                        await M_Config.main_con.send( m_python_message );

                        m_python_message = new PythonMessage( "m_predictions.environmental_trends()", {"dir": workspaceFolder} );
                        await M_Config.main_con.send( m_python_message );
                        
                        M_Logging.log("Result:", JSON.stringify(M_Config.main_con.result));

                        m_python_message = new PythonMessage( "m_predictions.m_keews()", {"dir": workspaceFolder} );
                        await M_Config.main_con.send( m_python_message );

                        m_python_message = new PythonMessage( "m_predictions.write_stats()", {"dir": workspaceFolder} );
                        await M_Config.main_con.send( m_python_message );

                        break;
                }
            },
            undefined,
            this.context.subscriptions
        );
    }

    private _getHtmlForWebview(webview: vscode.Webview): string {
        const htmlPath = path.join(this.context.extensionPath, 'resources', 'predictions.html');
        const htmlContent = fs.readFileSync(htmlPath, 'utf8');
        return htmlContent;
    }
}
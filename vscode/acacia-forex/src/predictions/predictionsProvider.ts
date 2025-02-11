import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';

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
            message => {
                switch (message.command) {
                    case 'showPredictions':
                        vscode.window.showInformationMessage('Predictions button clicked');
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
import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';
import { ManageDecisionsProvider } from './manageDecisionProvider';

export class DecisionsProvider implements vscode.WebviewViewProvider {
    public static readonly viewType = 'acacia-forex.decisions';

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
                    case 'openForm':
                        vscode.window.showInformationMessage('Opening form');
                        manageDecisionProvider(this.context);
                        break;
                }
            },
            undefined,
            this.context.subscriptions
        );   
    }

    private _getHtmlForWebview(webview: vscode.Webview): string {
        const htmlPath = path.join(this.context.extensionPath, 'resources', 'decisions.html');
        const htmlContent = fs.readFileSync(htmlPath, 'utf8');

        return htmlContent;
    }

 
}

export function manageDecisionProvider(context: vscode.ExtensionContext) {
    const provider = new ManageDecisionsProvider(context);
    provider.showManageDecisionsWebview();

}


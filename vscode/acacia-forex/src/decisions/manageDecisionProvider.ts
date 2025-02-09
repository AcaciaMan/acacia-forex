import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';

export class ManageDecisionsProvider {
    constructor(private context: vscode.ExtensionContext) {}

    public showManageDecisionsWebview() {
        const panel = vscode.window.createWebviewPanel(
            'acacia-forex.manageDecisions',
            'Manage Forex Decisions',
            vscode.ViewColumn.One,
            {
                enableScripts: true
            }
        );

        const htmlPath = path.join(this.context.extensionPath, 'resources', 'manageDecisions.html');
        const htmlContent = fs.readFileSync(htmlPath, 'utf8');
        panel.webview.html = htmlContent;

        // Load existing decisions from JSON file
        // JSON file is located in workspace folder
        const workspaceFolders = vscode.workspace.workspaceFolders;
        if (!workspaceFolders) {
            vscode.window.showErrorMessage('No workspace folder is open');
            return;
        }
        const decisionsPath = path.join(workspaceFolders[0].uri.fsPath, 'decisions.json');
        const decisionsContent = fs.readFileSync(decisionsPath, 'utf8');
        const decisions = JSON.parse(decisionsContent).decisions;

        panel.webview.onDidReceiveMessage(
            message => {
                switch (message.command) {
                    case 'addDecision':
                        this.addDecision(message.decision, decisionsPath, panel);
                        break;
                }
            },
            undefined,
            this.context.subscriptions
        );
        // Sort decisions in descending order by dateTime
        decisions.sort((a: any, b: any) => {
            return new Date(b.dateTime).getTime() - new Date(a.dateTime).getTime();
        });
        panel.webview.postMessage({ command: 'populateDecisions', decisions });
    }

    private addDecision(decision: any, decisionsPath: string, panel: vscode.WebviewPanel) {
        const decisionsContent = fs.readFileSync(decisionsPath, 'utf8');
        const decisions = JSON.parse(decisionsContent).decisions;
        decision.id = decisions.length ? decisions[decisions.length - 1].id + 1 : 1;
        decisions.push(decision);

        fs.writeFileSync(decisionsPath, JSON.stringify({ decisions }, null, 2));
        // Sort decisions in descending order by dateTime
        decisions.sort((a: any, b: any) => {
            return new Date(b.dateTime).getTime() - new Date(a.dateTime).getTime();
        });
        panel.webview.postMessage({ command: 'populateDecisions', decisions });
    }
}
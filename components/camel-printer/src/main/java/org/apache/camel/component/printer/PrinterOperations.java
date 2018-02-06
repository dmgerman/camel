begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|Doc
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocFlavor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocPrintJob
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintServiceLookup
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|HashPrintRequestAttributeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|PrintRequestAttributeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Copies
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|JobName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|MediaSizeName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Sides
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|PrinterOperations
specifier|public
class|class
name|PrinterOperations
implements|implements
name|PrinterOperationsInterface
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PrinterOperations
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|printService
specifier|private
name|PrintService
name|printService
decl_stmt|;
DECL|field|flavor
specifier|private
name|DocFlavor
name|flavor
decl_stmt|;
DECL|field|printRequestAttributeSet
specifier|private
name|PrintRequestAttributeSet
name|printRequestAttributeSet
decl_stmt|;
DECL|field|doc
specifier|private
name|Doc
name|doc
decl_stmt|;
DECL|method|PrinterOperations ()
specifier|public
name|PrinterOperations
parameter_list|()
throws|throws
name|PrintException
block|{
name|printService
operator|=
name|PrintServiceLookup
operator|.
name|lookupDefaultPrintService
argument_list|()
expr_stmt|;
if|if
condition|(
name|printService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PrintException
argument_list|(
literal|"Printer lookup failure. No default printer set up for this host"
argument_list|)
throw|;
block|}
name|flavor
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|AUTOSENSE
expr_stmt|;
name|printRequestAttributeSet
operator|=
operator|new
name|HashPrintRequestAttributeSet
argument_list|()
expr_stmt|;
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
operator|new
name|Copies
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
name|MediaSizeName
operator|.
name|NA_LETTER
argument_list|)
expr_stmt|;
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
name|Sides
operator|.
name|ONE_SIDED
argument_list|)
expr_stmt|;
block|}
DECL|method|PrinterOperations (PrintService printService, DocFlavor flavor, PrintRequestAttributeSet printRequestAttributeSet)
specifier|public
name|PrinterOperations
parameter_list|(
name|PrintService
name|printService
parameter_list|,
name|DocFlavor
name|flavor
parameter_list|,
name|PrintRequestAttributeSet
name|printRequestAttributeSet
parameter_list|)
throws|throws
name|PrintException
block|{
name|this
operator|.
name|setPrintService
argument_list|(
name|printService
argument_list|)
expr_stmt|;
name|this
operator|.
name|setFlavor
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPrintRequestAttributeSet
argument_list|(
name|printRequestAttributeSet
argument_list|)
expr_stmt|;
block|}
DECL|method|print (Doc doc, boolean sendToPrinter, String mimeType, String jobName)
specifier|public
name|void
name|print
parameter_list|(
name|Doc
name|doc
parameter_list|,
name|boolean
name|sendToPrinter
parameter_list|,
name|String
name|mimeType
parameter_list|,
name|String
name|jobName
parameter_list|)
throws|throws
name|PrintException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Print Service: "
operator|+
name|this
operator|.
name|printService
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sendToPrinter
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Print flag is set to false. This job will not be printed as long as this setting remains in effect. Please set the flag to true or remove the setting."
argument_list|)
expr_stmt|;
name|File
name|file
decl_stmt|;
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"GIF"
argument_list|)
operator|||
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"RENDERABLE_IMAGE"
argument_list|)
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"./target/PrintOutput_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|+
literal|".gif"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"JPEG"
argument_list|)
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"./target/PrintOutput_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|+
literal|".jpeg"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PDF"
argument_list|)
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"./target/PrintOutput_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|+
literal|".pdf"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"./target/PrintOutput_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing print job to file: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|doc
operator|.
name|getStreamForBytes
argument_list|()
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|in
argument_list|,
name|fos
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|fos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PrintException
argument_list|(
literal|"Error writing Document to the target file "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Issuing Job to Printer: {}"
argument_list|,
name|this
operator|.
name|printService
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|print
argument_list|(
name|doc
argument_list|,
name|jobName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|print (Doc doc, String jobName)
specifier|public
name|void
name|print
parameter_list|(
name|Doc
name|doc
parameter_list|,
name|String
name|jobName
parameter_list|)
throws|throws
name|PrintException
block|{
comment|// we need create a new job for each print
name|DocPrintJob
name|job
init|=
name|getPrintService
argument_list|()
operator|.
name|createPrintJob
argument_list|()
decl_stmt|;
name|PrintRequestAttributeSet
name|attrs
init|=
operator|new
name|HashPrintRequestAttributeSet
argument_list|(
name|printRequestAttributeSet
argument_list|)
decl_stmt|;
name|attrs
operator|.
name|add
argument_list|(
operator|new
name|JobName
argument_list|(
name|jobName
argument_list|,
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|job
operator|.
name|print
argument_list|(
name|doc
argument_list|,
name|attrs
argument_list|)
expr_stmt|;
block|}
DECL|method|getPrintService ()
specifier|public
name|PrintService
name|getPrintService
parameter_list|()
block|{
return|return
name|printService
return|;
block|}
DECL|method|setPrintService (PrintService printService)
specifier|public
name|void
name|setPrintService
parameter_list|(
name|PrintService
name|printService
parameter_list|)
block|{
name|this
operator|.
name|printService
operator|=
name|printService
expr_stmt|;
block|}
DECL|method|getFlavor ()
specifier|public
name|DocFlavor
name|getFlavor
parameter_list|()
block|{
return|return
name|flavor
return|;
block|}
DECL|method|setFlavor (DocFlavor flavor)
specifier|public
name|void
name|setFlavor
parameter_list|(
name|DocFlavor
name|flavor
parameter_list|)
block|{
name|this
operator|.
name|flavor
operator|=
name|flavor
expr_stmt|;
block|}
DECL|method|getPrintRequestAttributeSet ()
specifier|public
name|PrintRequestAttributeSet
name|getPrintRequestAttributeSet
parameter_list|()
block|{
return|return
name|printRequestAttributeSet
return|;
block|}
DECL|method|setPrintRequestAttributeSet (PrintRequestAttributeSet printRequestAttributeSet)
specifier|public
name|void
name|setPrintRequestAttributeSet
parameter_list|(
name|PrintRequestAttributeSet
name|printRequestAttributeSet
parameter_list|)
block|{
name|this
operator|.
name|printRequestAttributeSet
operator|=
name|printRequestAttributeSet
expr_stmt|;
block|}
DECL|method|getDoc ()
specifier|public
name|Doc
name|getDoc
parameter_list|()
block|{
return|return
name|doc
return|;
block|}
DECL|method|setDoc (Doc doc)
specifier|public
name|void
name|setDoc
parameter_list|(
name|Doc
name|doc
parameter_list|)
block|{
name|this
operator|.
name|doc
operator|=
name|doc
expr_stmt|;
block|}
block|}
end_class

end_unit


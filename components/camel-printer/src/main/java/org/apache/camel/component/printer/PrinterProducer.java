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
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|Exchange
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
name|impl
operator|.
name|DefaultProducer
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|PrinterProducer
specifier|public
class|class
name|PrinterProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PrinterProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|PrinterConfiguration
name|config
decl_stmt|;
DECL|field|printerOperations
specifier|private
name|PrinterOperations
name|printerOperations
decl_stmt|;
DECL|field|printService
specifier|private
name|PrintService
name|printService
decl_stmt|;
DECL|field|printer
specifier|private
name|String
name|printer
decl_stmt|;
DECL|method|PrinterProducer (Endpoint endpoint, PrinterConfiguration config)
specifier|public
name|PrinterProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|PrinterConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|print
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
DECL|method|print (InputStream body)
specifier|private
name|void
name|print
parameter_list|(
name|InputStream
name|body
parameter_list|)
throws|throws
name|PrintException
block|{
if|if
condition|(
name|printerOperations
operator|.
name|getPrintService
argument_list|()
operator|.
name|isDocFlavorSupported
argument_list|(
name|printerOperations
operator|.
name|getFlavor
argument_list|()
argument_list|)
condition|)
block|{
name|PrintDocument
name|printDoc
init|=
operator|new
name|PrintDocument
argument_list|(
name|body
argument_list|,
name|printerOperations
operator|.
name|getFlavor
argument_list|()
argument_list|)
decl_stmt|;
name|printerOperations
operator|.
name|print
argument_list|(
name|printDoc
argument_list|,
name|config
operator|.
name|getCopies
argument_list|()
argument_list|,
name|config
operator|.
name|isSendToPrinter
argument_list|()
argument_list|,
name|config
operator|.
name|getMimeType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assignDocFlavor ()
specifier|private
name|DocFlavor
name|assignDocFlavor
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|config
operator|.
name|getDocFlavor
argument_list|()
return|;
block|}
DECL|method|assignPrintAttributes ()
specifier|private
name|PrintRequestAttributeSet
name|assignPrintAttributes
parameter_list|()
throws|throws
name|PrintException
block|{
name|PrintRequestAttributeSet
name|printRequestAttributeSet
init|=
operator|new
name|HashPrintRequestAttributeSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getCopies
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
operator|new
name|Copies
argument_list|(
name|config
operator|.
name|getCopies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|PrintException
argument_list|(
literal|"Number of print copies should be greater than zero"
argument_list|)
throw|;
block|}
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
name|config
operator|.
name|getMediaSizeName
argument_list|()
argument_list|)
expr_stmt|;
name|printRequestAttributeSet
operator|.
name|add
argument_list|(
name|config
operator|.
name|getInternalSides
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|printRequestAttributeSet
return|;
block|}
DECL|method|assignPrintJob (PrintService printService)
specifier|private
name|DocPrintJob
name|assignPrintJob
parameter_list|(
name|PrintService
name|printService
parameter_list|)
block|{
return|return
name|printService
operator|.
name|createPrintJob
argument_list|()
return|;
block|}
DECL|method|assignPrintService ()
specifier|private
name|PrintService
name|assignPrintService
parameter_list|()
throws|throws
name|PrintException
block|{
name|PrintService
name|printService
decl_stmt|;
if|if
condition|(
operator|(
name|config
operator|.
name|getHostname
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"localhost"
argument_list|)
operator|)
operator|&&
operator|(
name|config
operator|.
name|getPrintername
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"/default"
argument_list|)
operator|)
condition|)
block|{
name|printService
operator|=
name|PrintServiceLookup
operator|.
name|lookupDefaultPrintService
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|PrintService
index|[]
name|services
init|=
name|PrintServiceLookup
operator|.
name|lookupPrintServices
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|setPrinter
argument_list|(
literal|"\\\\"
operator|+
name|config
operator|.
name|getHostname
argument_list|()
operator|+
literal|"\\"
operator|+
name|config
operator|.
name|getPrintername
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|position
init|=
name|findPrinter
argument_list|(
name|services
argument_list|,
name|printer
argument_list|)
decl_stmt|;
if|if
condition|(
name|position
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|PrintException
argument_list|(
literal|"No printer found with name: "
operator|+
name|printer
operator|+
literal|". Please verify that the host and printer are registered and reachable from this machine."
argument_list|)
throw|;
block|}
name|printService
operator|=
name|services
index|[
name|position
index|]
expr_stmt|;
block|}
return|return
name|printService
return|;
block|}
DECL|method|findPrinter (PrintService[] services, String printer)
specifier|private
name|int
name|findPrinter
parameter_list|(
name|PrintService
index|[]
name|services
parameter_list|,
name|String
name|printer
parameter_list|)
block|{
name|int
name|position
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|services
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|printer
operator|.
name|equalsIgnoreCase
argument_list|(
name|services
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|position
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
return|return
name|position
return|;
block|}
DECL|method|getConfig ()
specifier|public
name|PrinterConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|getPrinterOperations ()
specifier|public
name|PrinterOperations
name|getPrinterOperations
parameter_list|()
block|{
return|return
name|printerOperations
return|;
block|}
DECL|method|setPrinterOperations (PrinterOperations printerOperations)
specifier|public
name|void
name|setPrinterOperations
parameter_list|(
name|PrinterOperations
name|printerOperations
parameter_list|)
block|{
name|this
operator|.
name|printerOperations
operator|=
name|printerOperations
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
DECL|method|getPrinter ()
specifier|public
name|String
name|getPrinter
parameter_list|()
block|{
return|return
name|printer
return|;
block|}
DECL|method|setPrinter (String printer)
specifier|public
name|void
name|setPrinter
parameter_list|(
name|String
name|printer
parameter_list|)
block|{
name|this
operator|.
name|printer
operator|=
name|printer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|printService
operator|==
literal|null
condition|)
block|{
name|printService
operator|=
name|assignPrintService
argument_list|()
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|printService
argument_list|,
literal|"PrintService"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|printerOperations
operator|==
literal|null
condition|)
block|{
name|printerOperations
operator|=
operator|new
name|PrinterOperations
argument_list|(
name|printService
argument_list|,
name|assignPrintJob
argument_list|(
name|printService
argument_list|)
argument_list|,
name|assignDocFlavor
argument_list|()
argument_list|,
name|assignPrintAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit


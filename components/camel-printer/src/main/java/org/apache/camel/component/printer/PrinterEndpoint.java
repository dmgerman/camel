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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_comment
comment|/**  * The printer component is used for sending messages to printers as print jobs.  *  * Obviously the payload has to be a formatted piece of payload in order for the component to appropriately print it.  * The objective is to be able to direct specific payloads as jobs to a line printer in a camel flow.  *  * The functionality allows for the payload to be printed on a default printer, named local, remote or wirelessly  * linked printer using the javax printing API under the covers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.1.0"
argument_list|,
name|scheme
operator|=
literal|"lpr"
argument_list|,
name|title
operator|=
literal|"Printer"
argument_list|,
name|syntax
operator|=
literal|"lpr:hostname:port/printername"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"printing"
argument_list|)
DECL|class|PrinterEndpoint
specifier|public
class|class
name|PrinterEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|JOB_NAME
specifier|public
specifier|static
specifier|final
name|String
name|JOB_NAME
init|=
literal|"PrinterJobName"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|config
specifier|private
name|PrinterConfiguration
name|config
decl_stmt|;
DECL|method|PrinterEndpoint ()
specifier|public
name|PrinterEndpoint
parameter_list|()
block|{     }
DECL|method|PrinterEndpoint (String endpointUri, Component component, PrinterConfiguration config)
specifier|public
name|PrinterEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|PrinterConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot create a consumer for a Printer endpoint"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|PrinterProducer
argument_list|(
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
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
DECL|method|setConfig (PrinterConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|PrinterConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
block|}
end_class

end_unit


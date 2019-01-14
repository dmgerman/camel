begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|sheets
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|Sheets
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|Spreadsheet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|ValueRange
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
name|Message
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
name|component
operator|.
name|google
operator|.
name|sheets
operator|.
name|GoogleSheetsClientFactory
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
name|ScheduledPollEndpoint
import|;
end_import

begin_comment
comment|/**  * The google-sheets component provides access to Google Sheets.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"google-sheets-stream"
argument_list|,
name|title
operator|=
literal|"Google Sheets Stream"
argument_list|,
name|syntax
operator|=
literal|"google-sheets-stream:apiName"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"api,cloud,sheets"
argument_list|)
DECL|class|GoogleSheetsStreamEndpoint
specifier|public
class|class
name|GoogleSheetsStreamEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|GoogleSheetsStreamConfiguration
name|configuration
decl_stmt|;
DECL|method|GoogleSheetsStreamEndpoint (String uri, GoogleSheetsStreamComponent component, GoogleSheetsStreamConfiguration endpointConfiguration)
specifier|public
name|GoogleSheetsStreamEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GoogleSheetsStreamComponent
name|component
parameter_list|,
name|GoogleSheetsStreamConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The camel google sheets stream component doesn't support producer"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
specifier|final
name|GoogleSheetsStreamConsumer
name|consumer
init|=
operator|new
name|GoogleSheetsStreamConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|getClient ()
specifier|public
name|Sheets
name|getClient
parameter_list|()
block|{
return|return
operator|(
operator|(
name|GoogleSheetsStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClient
argument_list|(
name|configuration
argument_list|)
return|;
block|}
DECL|method|getClientFactory ()
specifier|public
name|GoogleSheetsClientFactory
name|getClientFactory
parameter_list|()
block|{
return|return
operator|(
operator|(
name|GoogleSheetsStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClientFactory
argument_list|()
return|;
block|}
DECL|method|setClientFactory (GoogleSheetsClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleSheetsClientFactory
name|clientFactory
parameter_list|)
block|{
operator|(
operator|(
name|GoogleSheetsStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|setClientFactory
argument_list|(
name|clientFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|GoogleSheetsStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExchange (ValueRange valueRange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ValueRange
name|valueRange
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleSheetsStreamConstants
operator|.
name|SPREADSHEET_ID
argument_list|,
name|configuration
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleSheetsStreamConstants
operator|.
name|RANGE
argument_list|,
name|valueRange
operator|.
name|getRange
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleSheetsStreamConstants
operator|.
name|MAJOR_DIMENSION
argument_list|,
name|valueRange
operator|.
name|getMajorDimension
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|valueRange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createExchange (Spreadsheet spreadsheet)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Spreadsheet
name|spreadsheet
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleSheetsStreamConstants
operator|.
name|SPREADSHEET_ID
argument_list|,
name|spreadsheet
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleSheetsStreamConstants
operator|.
name|SPREADSHEET_URL
argument_list|,
name|spreadsheet
operator|.
name|getSpreadsheetUrl
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|spreadsheet
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit


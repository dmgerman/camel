begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * Base class for DataSet  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DataSetSupport
specifier|public
specifier|abstract
class|class
name|DataSetSupport
implements|implements
name|DataSet
block|{
DECL|field|defaultHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|defaultHeaders
decl_stmt|;
DECL|field|outputTransformer
specifier|private
name|Processor
name|outputTransformer
decl_stmt|;
DECL|field|size
specifier|private
name|long
name|size
init|=
literal|10
decl_stmt|;
DECL|field|reportCount
specifier|private
name|long
name|reportCount
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|DataSetSupport ()
specifier|public
name|DataSetSupport
parameter_list|()
block|{     }
DECL|method|DataSetSupport (int size)
specifier|public
name|DataSetSupport
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|setSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|populateMessage (Exchange exchange, long messageIndex)
specifier|public
name|void
name|populateMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|messageIndex
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|createMessageBody
argument_list|(
name|messageIndex
argument_list|)
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeaders
argument_list|(
name|getDefaultHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|applyHeaders
argument_list|(
name|exchange
argument_list|,
name|messageIndex
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputTransformer
operator|!=
literal|null
condition|)
block|{
name|outputTransformer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertMessageExpected (DataSetEndpoint dataSetEndpoint, Exchange expected, Exchange actual, long index)
specifier|public
name|void
name|assertMessageExpected
parameter_list|(
name|DataSetEndpoint
name|dataSetEndpoint
parameter_list|,
name|Exchange
name|expected
parameter_list|,
name|Exchange
name|actual
parameter_list|,
name|long
name|index
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|expectedBody
init|=
name|expected
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|actualBody
init|=
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedBody
operator|!=
literal|null
condition|)
block|{
comment|// lets coerce to the correct type
name|actualBody
operator|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|actual
argument_list|,
name|expectedBody
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DataSetEndpoint
operator|.
name|assertEquals
argument_list|(
literal|"message body"
argument_list|,
name|expectedBody
argument_list|,
name|actualBody
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getSize ()
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|setSize (long size)
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
DECL|method|getReportCount ()
specifier|public
name|long
name|getReportCount
parameter_list|()
block|{
if|if
condition|(
name|reportCount
operator|<=
literal|0
condition|)
block|{
name|reportCount
operator|=
name|getSize
argument_list|()
operator|/
literal|5
expr_stmt|;
block|}
return|return
name|reportCount
return|;
block|}
comment|/**      * Sets the number of messages in a group on which we will report that messages have been received.      */
DECL|method|setReportCount (long reportCount)
specifier|public
name|void
name|setReportCount
parameter_list|(
name|long
name|reportCount
parameter_list|)
block|{
name|this
operator|.
name|reportCount
operator|=
name|reportCount
expr_stmt|;
block|}
DECL|method|getDefaultHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getDefaultHeaders
parameter_list|()
block|{
if|if
condition|(
name|defaultHeaders
operator|==
literal|null
condition|)
block|{
name|defaultHeaders
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|populateDefaultHeaders
argument_list|(
name|defaultHeaders
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultHeaders
return|;
block|}
DECL|method|setDefaultHeaders (Map<String, Object> defaultHeaders)
specifier|public
name|void
name|setDefaultHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|defaultHeaders
parameter_list|)
block|{
name|this
operator|.
name|defaultHeaders
operator|=
name|defaultHeaders
expr_stmt|;
block|}
DECL|method|getOutputTransformer ()
specifier|public
name|Processor
name|getOutputTransformer
parameter_list|()
block|{
return|return
name|outputTransformer
return|;
block|}
DECL|method|setOutputTransformer (Processor outputTransformer)
specifier|public
name|void
name|setOutputTransformer
parameter_list|(
name|Processor
name|outputTransformer
parameter_list|)
block|{
name|this
operator|.
name|outputTransformer
operator|=
name|outputTransformer
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createMessageBody (long messageIndex)
specifier|protected
specifier|abstract
name|Object
name|createMessageBody
parameter_list|(
name|long
name|messageIndex
parameter_list|)
function_decl|;
comment|/**      * Allows derived classes to add some custom headers for a given message      */
DECL|method|applyHeaders (Exchange exchange, long messageIndex)
specifier|protected
name|void
name|applyHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|messageIndex
parameter_list|)
block|{     }
comment|/**      * Allows derived classes to customize a default set of properties      */
DECL|method|populateDefaultHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateDefaultHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{     }
block|}
end_class

end_unit


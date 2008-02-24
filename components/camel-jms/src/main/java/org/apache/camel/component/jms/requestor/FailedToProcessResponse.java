begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.requestor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|requestor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * An exception thrown if a response message from an InOut could not be processed  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|FailedToProcessResponse
specifier|public
class|class
name|FailedToProcessResponse
extends|extends
name|RuntimeCamelException
block|{
DECL|field|response
specifier|private
specifier|final
name|Message
name|response
decl_stmt|;
DECL|method|FailedToProcessResponse (Message response, JMSException e)
specifier|public
name|FailedToProcessResponse
parameter_list|(
name|Message
name|response
parameter_list|,
name|JMSException
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to process response: "
operator|+
name|e
operator|+
literal|". Message: "
operator|+
name|response
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
comment|/**      * The response message which caused the exception      *       * @return      */
DECL|method|getResponse ()
specifier|public
name|Message
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
block|}
end_class

end_unit


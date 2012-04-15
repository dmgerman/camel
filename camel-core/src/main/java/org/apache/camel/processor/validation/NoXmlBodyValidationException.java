begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
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
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * An exception found if no XML body is available on the inbound message  *  * @version   */
end_comment

begin_class
DECL|class|NoXmlBodyValidationException
specifier|public
class|class
name|NoXmlBodyValidationException
extends|extends
name|ValidationException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4502520681354358599L
decl_stmt|;
DECL|method|NoXmlBodyValidationException (Exchange exchange)
specifier|public
name|NoXmlBodyValidationException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|,
literal|"No XML body could be found on the input message"
argument_list|)
expr_stmt|;
block|}
DECL|method|NoXmlBodyValidationException (Exchange exchange, Throwable cause)
specifier|public
name|NoXmlBodyValidationException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"No XML body could be found on the input message"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


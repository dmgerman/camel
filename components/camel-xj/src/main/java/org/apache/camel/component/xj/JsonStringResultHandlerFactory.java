begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xj
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonFactory
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
name|component
operator|.
name|xslt
operator|.
name|ResultHandler
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
name|xslt
operator|.
name|ResultHandlerFactory
import|;
end_import

begin_comment
comment|/**  * {@JsonStringResultHandler} factory  */
end_comment

begin_class
DECL|class|JsonStringResultHandlerFactory
specifier|public
class|class
name|JsonStringResultHandlerFactory
implements|implements
name|ResultHandlerFactory
block|{
DECL|field|jsonFactory
specifier|private
specifier|final
name|JsonFactory
name|jsonFactory
decl_stmt|;
comment|/**      * Creates a new json to string result handler factory      * @param jsonFactory the {@link JsonFactory} to use to write the json.      */
DECL|method|JsonStringResultHandlerFactory (JsonFactory jsonFactory)
specifier|public
name|JsonStringResultHandlerFactory
parameter_list|(
name|JsonFactory
name|jsonFactory
parameter_list|)
block|{
name|this
operator|.
name|jsonFactory
operator|=
name|jsonFactory
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createResult (Exchange exchange)
specifier|public
name|ResultHandler
name|createResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|JsonStringResultHandler
argument_list|(
name|jsonFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit


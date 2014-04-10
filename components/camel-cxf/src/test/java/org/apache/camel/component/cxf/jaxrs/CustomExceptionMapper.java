begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ExceptionMapper
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
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|CustomException
import|;
end_import

begin_class
DECL|class|CustomExceptionMapper
specifier|public
class|class
name|CustomExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|CustomException
argument_list|>
block|{
annotation|@
name|Override
DECL|method|toResponse (CustomException exception)
specifier|public
name|Response
name|toResponse
parameter_list|(
name|CustomException
name|exception
parameter_list|)
block|{
name|Response
operator|.
name|Status
name|status
decl_stmt|;
name|status
operator|=
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
expr_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
name|status
argument_list|)
operator|.
name|header
argument_list|(
literal|"exception"
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit


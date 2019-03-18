begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.transformer.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|transformer
operator|.
name|cdi
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * Configures all our Camel routes, components, endpoints and beans  */
end_comment

begin_class
DECL|class|MyRoutes
specifier|public
class|class
name|MyRoutes
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
literal|"xml:MyRequest"
argument_list|)
operator|.
name|toType
argument_list|(
literal|"xml:MyResponse"
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"xslt:transform.xsl"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:foo?period=5000"
argument_list|)
operator|.
name|id
argument_list|(
literal|"timer-route"
argument_list|)
operator|.
name|log
argument_list|(
literal|"start -->"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"<MyRequest>foobar</MyRequest>"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"--> Sending:[${body}]"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"--> Received:[${body}]"
argument_list|)
operator|.
name|log
argument_list|(
literal|"<-- end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|id
argument_list|(
literal|"xslt-route"
argument_list|)
operator|.
name|inputType
argument_list|(
literal|"xml:MyRequest"
argument_list|)
operator|.
name|outputType
argument_list|(
literal|"xml:MyResponse"
argument_list|)
operator|.
name|log
argument_list|(
literal|"----> Received:[${body}]"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


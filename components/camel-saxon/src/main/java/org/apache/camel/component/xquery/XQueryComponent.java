begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

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
name|builder
operator|.
name|saxon
operator|.
name|XQueryBuilder
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
name|ResourceBasedComponent
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
name|ProcessorEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * An<a href="http://activemq.apache.org/camel/xquery.html">XQuery Component</a>  * for performing transforming messages  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|XQueryComponent
specifier|public
class|class
name|XQueryComponent
extends|extends
name|ResourceBasedComponent
block|{
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Resource
name|resource
init|=
name|resolveMandatoryResource
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
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
name|this
operator|+
literal|" using schema resource: "
operator|+
name|resource
argument_list|)
expr_stmt|;
block|}
name|XQueryBuilder
name|xslt
init|=
name|XQueryBuilder
operator|.
name|xquery
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
decl_stmt|;
name|configureXslt
argument_list|(
name|xslt
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|xslt
argument_list|)
return|;
block|}
DECL|method|configureXslt (XQueryBuilder xQueryBuilder, String uri, String remaining, Map parameters)
specifier|protected
name|void
name|configureXslt
parameter_list|(
name|XQueryBuilder
name|xQueryBuilder
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|setProperties
argument_list|(
name|xQueryBuilder
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


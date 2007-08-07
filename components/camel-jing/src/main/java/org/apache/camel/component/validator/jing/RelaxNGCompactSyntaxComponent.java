begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator.jing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
operator|.
name|jing
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

begin_comment
comment|/**  * A component for validating the XML payload using  *<a href="http://www.oasis-open.org/committees/relax-ng/compact-20021121.html">RelaxNG Compact Syntax</a> using the  *<a href="http://www.thaiopensource.com/relaxng/jing.html">Jing library</a>  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|RelaxNGCompactSyntaxComponent
specifier|public
class|class
name|RelaxNGCompactSyntaxComponent
extends|extends
name|JingComponent
block|{
DECL|method|configureValidator (JingValidator validator, String uri, String remaining, Map parameters)
specifier|protected
name|void
name|configureValidator
parameter_list|(
name|JingValidator
name|validator
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
name|validator
operator|.
name|setCompactSyntax
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|configureValidator
argument_list|(
name|validator
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit


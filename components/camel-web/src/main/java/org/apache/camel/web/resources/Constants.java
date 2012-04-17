begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|Constants
specifier|public
specifier|final
class|class
name|Constants
block|{
DECL|field|HTML_MIME_TYPES
specifier|public
specifier|static
specifier|final
name|String
name|HTML_MIME_TYPES
init|=
literal|"text/html;qs=5"
decl_stmt|;
DECL|field|DATA_MIME_TYPES
specifier|public
specifier|static
specifier|final
name|String
name|DATA_MIME_TYPES
init|=
literal|"text/xml,application/xml,application/json"
decl_stmt|;
DECL|field|DOT_MIMETYPE
specifier|public
specifier|static
specifier|final
name|String
name|DOT_MIMETYPE
init|=
literal|"text/vnd.graphviz"
decl_stmt|;
DECL|field|JAXB_PACKAGES
specifier|public
specifier|static
specifier|final
name|String
name|JAXB_PACKAGES
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
operator|+
literal|":org.apache.camel.web.model"
decl_stmt|;
DECL|method|Constants ()
specifier|private
name|Constants
parameter_list|()
block|{     }
block|}
end_class

end_unit


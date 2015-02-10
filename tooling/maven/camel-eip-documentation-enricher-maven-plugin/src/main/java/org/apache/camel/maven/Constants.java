begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_comment
comment|/**  * Stores mojo related constants.  */
end_comment

begin_class
DECL|class|Constants
specifier|public
specifier|final
class|class
name|Constants
block|{
comment|// Camel core constants.
DECL|field|PATH_TO_MODEL_DIR
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_MODEL_DIR
init|=
literal|"target/classes/org/apache/camel/model"
decl_stmt|;
comment|// XML constants.
DECL|field|NAME_ATTRIBUTE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|NAME_ATTRIBUTE_NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|TYPE_ATTRIBUTE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ATTRIBUTE_NAME
init|=
literal|"type"
decl_stmt|;
DECL|field|XS_ANNOTATION_ELEMENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XS_ANNOTATION_ELEMENT_NAME
init|=
literal|"xs:annotation"
decl_stmt|;
DECL|field|XS_DOCUMENTATION_ELEMENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XS_DOCUMENTATION_ELEMENT_NAME
init|=
literal|"xs:documentation"
decl_stmt|;
comment|// Json files constants.
DECL|field|PROPERTIES_ATTRIBUTE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTIES_ATTRIBUTE_NAME
init|=
literal|"properties"
decl_stmt|;
DECL|field|JSON_SUFIX
specifier|public
specifier|static
specifier|final
name|String
name|JSON_SUFIX
init|=
literal|".json"
decl_stmt|;
DECL|field|DESCRIPTION_ATTRIBUTE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTION_ATTRIBUTE_NAME
init|=
literal|"description"
decl_stmt|;
DECL|field|MODEL_ATTRIBUTE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|MODEL_ATTRIBUTE_NAME
init|=
literal|"model"
decl_stmt|;
DECL|method|Constants ()
specifier|private
name|Constants
parameter_list|()
block|{     }
block|}
end_class

end_unit


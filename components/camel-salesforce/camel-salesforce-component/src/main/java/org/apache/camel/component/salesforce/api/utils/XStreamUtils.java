begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|basic
operator|.
name|DateConverter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|reflection
operator|.
name|FieldDictionary
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|reflection
operator|.
name|PureJavaReflectionProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|core
operator|.
name|TreeMarshallingStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|naming
operator|.
name|NoNameCoder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|CompactWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|XppDriver
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|security
operator|.
name|AnyTypePermission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|security
operator|.
name|ExplicitTypePermission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|security
operator|.
name|TypePermission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|security
operator|.
name|WildcardTypePermission
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AnnotationFieldKeySorter
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|RestChoices
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|RestErrors
import|;
end_import

begin_class
DECL|class|XStreamUtils
specifier|public
specifier|final
class|class
name|XStreamUtils
block|{
DECL|field|PERMISSIONS_PROPERTY_DEFAULT
specifier|private
specifier|static
specifier|final
name|String
name|PERMISSIONS_PROPERTY_DEFAULT
init|=
literal|"java.lang.*,java.util.*"
decl_stmt|;
DECL|field|PERMISSIONS_PROPERTY_KEY
specifier|private
specifier|static
specifier|final
name|String
name|PERMISSIONS_PROPERTY_KEY
init|=
literal|"org.apache.camel.xstream.permissions"
decl_stmt|;
DECL|method|XStreamUtils ()
specifier|private
name|XStreamUtils
parameter_list|()
block|{     }
DECL|method|addDefaultPermissions (final XStream xstream)
specifier|public
specifier|static
name|void
name|addDefaultPermissions
parameter_list|(
specifier|final
name|XStream
name|xstream
parameter_list|)
block|{
name|addPermissions
argument_list|(
name|xstream
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|PERMISSIONS_PROPERTY_KEY
argument_list|,
name|PERMISSIONS_PROPERTY_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addPermissions (final XStream xstream, final String permissions)
specifier|public
specifier|static
name|void
name|addPermissions
parameter_list|(
specifier|final
name|XStream
name|xstream
parameter_list|,
specifier|final
name|String
name|permissions
parameter_list|)
block|{
for|for
control|(
name|String
name|pterm
range|:
name|permissions
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|boolean
name|aod
decl_stmt|;
name|pterm
operator|=
name|pterm
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|pterm
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|aod
operator|=
literal|false
expr_stmt|;
name|pterm
operator|=
name|pterm
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|aod
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|pterm
operator|.
name|startsWith
argument_list|(
literal|"+"
argument_list|)
condition|)
block|{
name|pterm
operator|=
name|pterm
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
name|TypePermission
name|typePermission
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|pterm
argument_list|)
condition|)
block|{
comment|// accept or deny any
name|typePermission
operator|=
name|AnyTypePermission
operator|.
name|ANY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pterm
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
operator|<
literal|0
condition|)
block|{
comment|// exact type
name|typePermission
operator|=
operator|new
name|ExplicitTypePermission
argument_list|(
operator|new
name|String
index|[]
block|{
name|pterm
block|}
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pterm
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// wildcard type
name|typePermission
operator|=
operator|new
name|WildcardTypePermission
argument_list|(
operator|new
name|String
index|[]
block|{
name|pterm
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|typePermission
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|aod
condition|)
block|{
name|xstream
operator|.
name|addPermission
argument_list|(
name|typePermission
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xstream
operator|.
name|denyPermission
argument_list|(
name|typePermission
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createXStream (final Class<?>... additionalTypes)
specifier|public
specifier|static
name|XStream
name|createXStream
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|additionalTypes
parameter_list|)
block|{
specifier|final
name|PureJavaReflectionProvider
name|reflectionProvider
init|=
operator|new
name|PureJavaReflectionProvider
argument_list|(
operator|new
name|FieldDictionary
argument_list|(
operator|new
name|AnnotationFieldKeySorter
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// use NoNameCoder to avoid escaping __ in custom field names
comment|// and CompactWriter to avoid pretty printing
specifier|final
name|XppDriver
name|hierarchicalStreamDriver
init|=
operator|new
name|XppDriver
argument_list|(
operator|new
name|NoNameCoder
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|HierarchicalStreamWriter
name|createWriter
parameter_list|(
specifier|final
name|Writer
name|out
parameter_list|)
block|{
return|return
operator|new
name|CompactWriter
argument_list|(
name|out
argument_list|,
name|getNameCoder
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|XStream
name|result
init|=
operator|new
name|XStream
argument_list|(
name|reflectionProvider
argument_list|,
name|hierarchicalStreamDriver
argument_list|)
decl_stmt|;
name|result
operator|.
name|aliasSystemAttribute
argument_list|(
literal|null
argument_list|,
literal|"class"
argument_list|)
expr_stmt|;
name|result
operator|.
name|ignoreUnknownElements
argument_list|()
expr_stmt|;
name|XStreamUtils
operator|.
name|addDefaultPermissions
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
operator|new
name|DateConverter
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
name|LocalDateTimeConverter
operator|.
name|INSTANCE
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
name|OffsetDateTimeConverter
operator|.
name|INSTANCE
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
name|ZonedDateTimeConverter
operator|.
name|INSTANCE
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
name|InstantConverter
operator|.
name|INSTANCE
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|registerConverter
argument_list|(
name|OffsetTimeConverter
operator|.
name|INSTANCE
argument_list|,
name|XStream
operator|.
name|PRIORITY_VERY_HIGH
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMarshallingStrategy
argument_list|(
operator|new
name|TreeMarshallingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|processAnnotations
argument_list|(
name|RestErrors
operator|.
name|class
argument_list|)
expr_stmt|;
name|result
operator|.
name|processAnnotations
argument_list|(
name|RestChoices
operator|.
name|class
argument_list|)
expr_stmt|;
name|result
operator|.
name|processAnnotations
argument_list|(
name|additionalTypes
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit


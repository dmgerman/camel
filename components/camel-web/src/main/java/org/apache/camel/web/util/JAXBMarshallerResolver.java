begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
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
name|ext
operator|.
name|ContextResolver
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
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|xml
operator|.
name|bind
operator|.
name|marshaller
operator|.
name|NamespacePrefixMapper
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Provider
DECL|class|JAXBMarshallerResolver
specifier|public
class|class
name|JAXBMarshallerResolver
implements|implements
name|ContextResolver
argument_list|<
name|Marshaller
argument_list|>
block|{
DECL|field|contextResolver
specifier|private
name|JAXBContextResolver
name|contextResolver
decl_stmt|;
DECL|method|JAXBMarshallerResolver ()
specifier|public
name|JAXBMarshallerResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|contextResolver
operator|=
operator|new
name|JAXBContextResolver
argument_list|()
expr_stmt|;
block|}
DECL|method|getContext (Class<?> aClass)
specifier|public
name|Marshaller
name|getContext
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
try|try
block|{
name|JAXBContext
name|context
init|=
name|contextResolver
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|context
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|NamespacePrefixMapper
name|namespaceMapper
init|=
operator|new
name|NamespacePrefixMapper
argument_list|()
block|{
comment|/**                  * Returns a preferred prefix for the given namespace URI.                  *                  * This method is intended to be overrided by a derived class.                  *                  * @param namespaceUri                  *      The namespace URI for which the prefix needs to be found.                  *      Never be null. "" is used to denote the default namespace.                  * @param suggestion                  *      When the content tree has a suggestion for the prefix                  *      to the given namespaceUri, that suggestion is passed as a                  *      parameter. Typically this value comes from QName.getPrefix()                  *      to show the preference of the content tree. This parameter                  *      may be null, and this parameter may represent an already                  *      occupied prefix.                  * @param requirePrefix                  *      If this method is expected to return non-empty prefix.                  *      When this flag is true, it means that the given namespace URI                  *      cannot be set as the default namespace.                  *                  * @return                  *      null if there's no preferred prefix for the namespace URI.                  *      In this case, the system will generate a prefix for you.                  *                  *      Otherwise the system will try to use the returned prefix,                  *      but generally there's no guarantee if the prefix will be                  *      actually used or not.                  *                  *      return "" to map this namespace URI to the default namespace.                  *      Again, there's no guarantee that this preference will be                  *      honored.                  *                  *      If this method returns "" when requirePrefix=true, the return                  *      value will be ignored and the system will generate one.                  */
annotation|@
name|Override
specifier|public
name|String
name|getPreferredPrefix
parameter_list|(
name|String
name|namespaceUri
parameter_list|,
name|String
name|suggestion
parameter_list|,
name|boolean
name|requirePrefix
parameter_list|)
block|{
if|if
condition|(
name|namespaceUri
operator|.
name|equals
argument_list|(
literal|"http://camel.apache.org/schema/web"
argument_list|)
condition|)
block|{
return|return
literal|"w"
return|;
block|}
elseif|else
if|if
condition|(
name|namespaceUri
operator|.
name|equals
argument_list|(
literal|"http://camel.apache.org/schema/spring"
argument_list|)
condition|)
block|{
if|if
condition|(
name|requirePrefix
condition|)
block|{
return|return
literal|"c"
return|;
block|}
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|suggestion
return|;
block|}
block|}
block|}
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
literal|"com.sun.xml.bind.namespacePrefixMapper"
argument_list|,
name|namespaceMapper
argument_list|)
expr_stmt|;
return|return
name|marshaller
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit


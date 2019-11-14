begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt.saxon
package|package
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
name|saxon
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
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
name|XmlSourceHandlerFactoryImpl
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
name|XsltBuilder
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
name|support
operator|.
name|builder
operator|.
name|xml
operator|.
name|StAX2SAXSource
import|;
end_import

begin_class
DECL|class|XsltSaxonBuilder
specifier|public
class|class
name|XsltSaxonBuilder
extends|extends
name|XsltBuilder
block|{
DECL|field|allowStAX
specifier|private
name|boolean
name|allowStAX
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
DECL|method|prepareSource (Source source)
specifier|protected
name|Source
name|prepareSource
parameter_list|(
name|Source
name|source
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAllowStAX
argument_list|()
operator|&&
name|source
operator|instanceof
name|StAXSource
condition|)
block|{
comment|// Always convert StAXSource to SAXSource.
comment|// * Xalan and Saxon-B don't support StAXSource.
comment|// * The JDK default implementation (XSLTC) doesn't handle CDATA events
comment|//   (see com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX).
comment|// * Saxon-HE/PE/EE seem to support StAXSource, but don't advertise this
comment|//   officially (via TransformerFactory.getFeature(StAXSource.FEATURE))
name|source
operator|=
operator|new
name|StAX2SAXSource
argument_list|(
operator|(
operator|(
name|StAXSource
operator|)
name|source
operator|)
operator|.
name|getXMLStreamReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|source
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|isAllowStAX ()
specifier|public
name|boolean
name|isAllowStAX
parameter_list|()
block|{
return|return
name|allowStAX
return|;
block|}
DECL|method|setAllowStAX (boolean allowStAX)
specifier|public
name|void
name|setAllowStAX
parameter_list|(
name|boolean
name|allowStAX
parameter_list|)
block|{
name|this
operator|.
name|allowStAX
operator|=
name|allowStAX
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createXmlSourceHandlerFactoryImpl ()
specifier|protected
name|XmlSourceHandlerFactoryImpl
name|createXmlSourceHandlerFactoryImpl
parameter_list|()
block|{
return|return
operator|new
name|SaxonXmlSourceHandlerFactoryImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit


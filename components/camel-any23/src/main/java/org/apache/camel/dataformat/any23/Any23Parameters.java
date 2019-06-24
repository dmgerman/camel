begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * To change this license header, choose License Headers in Project Properties.  * To change this template file, choose Tools | Templates  * and open the template in the editor.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.any23
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|any23
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|writer
operator|.
name|NTriplesWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|writer
operator|.
name|TripleHandler
import|;
end_import

begin_comment
comment|/**  *  * @author joe  */
end_comment

begin_class
DECL|class|Any23Parameters
specifier|public
class|class
name|Any23Parameters
block|{
DECL|field|OUT
specifier|private
name|ByteArrayOutputStream
name|OUT
decl_stmt|;
comment|//public static  final TripleHandler TRIPLEHANDLER ;
DECL|field|triplehandler
specifier|private
name|TripleHandler
name|triplehandler
decl_stmt|;
DECL|method|getTripleHandlerOutput ()
specifier|public
name|TripleHandler
name|getTripleHandlerOutput
parameter_list|()
block|{
return|return
name|triplehandler
return|;
block|}
DECL|method|setTripleHandlerOutput (TripleHandler triplehandler)
specifier|public
name|void
name|setTripleHandlerOutput
parameter_list|(
name|TripleHandler
name|triplehandler
parameter_list|)
block|{
name|this
operator|.
name|triplehandler
operator|=
name|triplehandler
expr_stmt|;
block|}
DECL|method|Any23Parameters (ByteArrayOutputStream out)
specifier|public
name|Any23Parameters
parameter_list|(
name|ByteArrayOutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|OUT
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|triplehandler
operator|=
operator|new
name|NTriplesWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

